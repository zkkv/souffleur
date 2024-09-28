package com.github.zkkv.souffleur.interfaces

import com.intellij.codeInsight.inline.completion.InlineCompletionRequest

interface LanguageModel {
    fun suggest(request : InlineCompletionRequest) : String

    fun prompt(documentText : String, caretPosition : Int) : String {
        return """
            |"A user has started typing code. Your goal is to suggest a code snippet that 
            |is the most fitting, given the context. 
            |
            |**IMPORTANT**: Your response MUST consist **only** of the part of the code that comes 
            |after the user's caret position. 
            |
            |Example: user so far has typed
            |```
            |public int sum(int a, int b) {
            |    retu
            |```
            |
            |The rest of the document reads
            |```
            |}
            |```
            |
            |Based on that, a sensible suggestion (i.e. your response) would be "rn a + b;". It is a good
            |guess because the function is called 'sum' and takes in two numbers. Notice, how your
            |response **does not** include the word `return` or the beginning `retu`. This is an 
            |example that should be followed for all your responses. 
            |
            |**IMPORTANT**: Don't provide any formatting, comments or explanation.
            |
            |Below you can find the *actual* document contents together with the user's input.
            |
            |```
            |${documentText.substring(0, caretPosition)}
            |```
            |
            |The response you give will appear here.
            |
            |Below you can find the rest of the document contents.
            |
            |```
            |${documentText.substring(caretPosition)}
            |```"
        """.trimMargin()
    }
}
