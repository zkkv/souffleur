package com.github.zkkv.souffleur

import com.intellij.codeInsight.inline.completion.*
import com.intellij.codeInsight.inline.completion.elements.InlineCompletionGrayTextElement
import com.intellij.codeInsight.inline.completion.suggestion.InlineCompletionSingleSuggestion


class Souffleur : InlineCompletionProvider {
    override val id: InlineCompletionProviderID = InlineCompletionProviderID("Souffleur")

    override suspend fun getSuggestion(request: InlineCompletionRequest): InlineCompletionSingleSuggestion {
        val suggestionText = model.suggest(request)
        val suggestion = InlineCompletionGrayTextElement(suggestionText)

        return InlineCompletionSingleSuggestion.build {
            emit(suggestion)
        }
    }

    override fun isEnabled(event: InlineCompletionEvent): Boolean {
        return event is InlineCompletionEvent.DocumentChange
    }

    private val model = Ollama()
}
