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