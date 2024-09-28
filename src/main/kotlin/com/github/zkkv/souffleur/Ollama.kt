package com.github.zkkv.souffleur

import com.github.zkkv.souffleur.interfaces.LanguageModel
import com.intellij.codeInsight.inline.completion.InlineCompletionRequest
import java.io.BufferedReader
import java.io.InputStreamReader

class Ollama : LanguageModel {
    override fun suggest(request: InlineCompletionRequest): String {
        val documentText = request.document.text
        val caretPosition = request.endOffset

        val prompt = this.prompt(documentText, caretPosition)
        return this.query(prompt)
    }

    private fun prompt(documentText : String, caretPosition : Int) : String {
        return """
            |"A user has started typing code. Your goal is to suggest a code snippet that 
            |is the most fitting, given the context. 
            |
            |**IMPORTANT**: Your response MUST consists **only** of the part of the code that comes 
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

    private fun query(prompt : String) : String {
        val command = listOf("docker", "exec", "ollama", "ollama", "run", "llama3.2:1b", prompt)

        return try {
            val process = ProcessBuilder(command).start()

            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = StringBuilder()

            reader.use {
                it.lineSequence().forEach { line ->
                    output.appendLine(line)
                }
            }

            val exitCode = process.waitFor()
            if (exitCode != 0) {
                throw RuntimeException("Process exited with code: $exitCode")
            }

            output.toString().trim()
        } catch (e: Exception) {
            throw RuntimeException("Exception: ${e.message}")
        }
    }
}
