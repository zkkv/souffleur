package com.github.zkkv.souffleur.models

import com.github.zkkv.souffleur.structures.TrieCache
import com.github.zkkv.souffleur.helpers.ProcessExecutorHelper
import com.github.zkkv.souffleur.interfaces.Cache
import com.github.zkkv.souffleur.interfaces.LanguageModel
import com.intellij.codeInsight.inline.completion.InlineCompletionRequest

class Ollama : LanguageModel {
    override fun suggest(request: InlineCompletionRequest): String {
        val documentText = request.document.text
        val caretPosition = request.endOffset

        val prompt = this.prompt(documentText, caretPosition)
        return this.query(prompt)
    }

    private val model = "llama3.2:1b"
    private val cache: Cache = TrieCache()

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
