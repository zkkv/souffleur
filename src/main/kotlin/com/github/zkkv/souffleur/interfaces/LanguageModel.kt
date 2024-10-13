package com.github.zkkv.souffleur.interfaces

import com.intellij.codeInsight.inline.completion.InlineCompletionRequest

/**
 * Language model interface.
 */
interface LanguageModel {

    /**
     * Returns a suggestion based on request information.
     *
     * @param request request obtained from the IDE
     * @return suggestion from the language model
     */
    fun suggest(request : InlineCompletionRequest) : String

    /**
     * Returns a prompt that is used as an input to the language model.
     *
     * @param documentText entire document string
     * @param caretPosition position of the caret at which a suggestion was triggered
     * @return prompt used as an input
     */
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
            |```"
        """.trimMargin()
    }
}
