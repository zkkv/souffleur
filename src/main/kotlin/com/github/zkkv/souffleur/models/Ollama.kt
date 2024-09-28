package com.github.zkkv.souffleur.models

import com.github.zkkv.souffleur.helpers.ProcessExecutorHelper
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

    private fun query(prompt : String) : String {
        val command = listOf("docker", "exec", "ollama", "ollama", "run", model, prompt)
        return ProcessExecutorHelper.execute(command)
    }
}
