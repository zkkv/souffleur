package com.github.zkkv.souffleur.helpers

import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Executes processes and returns the result from stdout.
 */
object ProcessExecutorHelper {

    /**
     * Execute the command passed.
     *
     * @param command command to execute in exec (list) format
     * @return stdout result of the execution
     * @throws RuntimeException if the execution goes wrong
     */
    fun execute(command : List<String>) : String {
        try {
            val process = ProcessBuilder(command)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

            val exitCode = process.waitFor()
            if (exitCode != 0) {
                throw RuntimeException("Process exited with code: $exitCode")
            }

            return process.inputStream.bufferedReader().readText().trim()
        } catch (e: Exception) {
            throw RuntimeException("Exception: ${e.message}")
        }
    }
}