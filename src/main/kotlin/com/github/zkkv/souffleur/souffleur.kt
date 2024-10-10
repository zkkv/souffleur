package com.github.zkkv.souffleur

import com.github.zkkv.souffleur.interfaces.LanguageModel
import com.github.zkkv.souffleur.models.Ollama
import com.github.zkkv.souffleur.structures.TrieCache
import com.intellij.codeInsight.inline.completion.*
import com.intellij.codeInsight.inline.completion.elements.InlineCompletionGrayTextElement
import com.intellij.codeInsight.inline.completion.suggestion.InlineCompletionSingleSuggestion

/**
 * Entry point for Souffleur suggestions plugin.
 */
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

    /**
     * The underlying language model used to provide suggestions.
     */
    private val model: LanguageModel = Ollama(TrieCache())
}
