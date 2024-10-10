package com.github.zkkv.souffleur.helpers

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlin.test.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import java.io.ByteArrayInputStream

class ProcessExecutorHelperTest {

    @MockK
    lateinit var process: Process

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `process exits successfully and returns string`() {
        val command = listOf("echo", "hi, mom")
        val expected = "hi, mom"
        val expectedStream = ByteArrayInputStream(expected.toByteArray())

        mockkConstructor(ProcessBuilder::class)

        every { anyConstructed<ProcessBuilder>().start() } returns process
        every { process.waitFor() } returns 0
        every { process.inputStream } returns expectedStream

        assertEquals(expected, ProcessExecutorHelper.execute(command))
    }

    @Test
    fun `process returns untrimmed string`() {
        val command = listOf("echo", "   hi, mom ")
        val expected = "hi, mom"
        val expectedStream = ByteArrayInputStream(expected.toByteArray())

        mockkConstructor(ProcessBuilder::class)

        every { anyConstructed<ProcessBuilder>().start() } returns process
        every { process.waitFor() } returns 0
        every { process.inputStream } returns expectedStream

        assertEquals(expected, ProcessExecutorHelper.execute(command))
    }

    @Test
    fun `process exits with non-zero code and throws exception`() {
        val command = listOf("echo", "hi, mom")

        mockkConstructor(ProcessBuilder::class)

        every { anyConstructed<ProcessBuilder>().start() } returns process
        every { process.waitFor() } returns 1

        assertFailsWith<RuntimeException> {
            ProcessExecutorHelper.execute(command)
        }
    }

    @Test
    fun `process fails and throws exception`() {
        val command = listOf("echo", "hi, mom")

        mockkConstructor(ProcessBuilder::class)

        every { anyConstructed<ProcessBuilder>().start() } returns process
        every { process.waitFor() } throws NullPointerException()

        assertFailsWith<RuntimeException> {
            ProcessExecutorHelper.execute(command)
        }
    }
}
