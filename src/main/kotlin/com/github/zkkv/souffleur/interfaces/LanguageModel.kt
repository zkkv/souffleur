package com.github.zkkv.souffleur.interfaces

import com.intellij.codeInsight.inline.completion.InlineCompletionRequest

interface LanguageModel {
    fun suggest(request : InlineCompletionRequest) : String
}
