package com.github.zkkv.souffleur.models

import com.github.zkkv.souffleur.helpers.ProcessExecutorHelper
import com.github.zkkv.souffleur.interfaces.Cache
import com.github.zkkv.souffleur.interfaces.LanguageModel
import com.intellij.codeInsight.inline.completion.InlineCompletionRequest

/**
 * Ollama LLM.
 */
open class Ollama(private val cache: Cache) : LanguageModel {
    override fun suggest(request: InlineCompletionRequest): String {
        val documentText = request.document.text
        val caretPosition = request.endOffset

        val prompt = this.prompt(documentText, caretPosition)
        return this.query(prompt)
    }

    /**
     * Specific LLM model version.
     */
    protected open val model = "llama3.2:1b"

    /**
     * Helper method that first checks the cache for saved suggestions.
     */
    private fun query(prompt : String) : String {
        val cached = cache.retrieve(prompt)
        if (cached != null) {
            return cached
        }
        
        val response = try {
            val command = listOf("docker", "exec", "ollama", "ollama", "run", model, prompt)
            ProcessExecutorHelper.execute(command)
        } catch (e: RuntimeException) {
            System.err.println("${e.message}\nMake sure you're running docker container")
            return ""
        }
        cache.insert(prompt, response)
        return response
    }
}
