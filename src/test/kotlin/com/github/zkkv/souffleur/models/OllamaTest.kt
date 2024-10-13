package com.github.zkkv.souffleur.models

import com.github.zkkv.souffleur.helpers.ProcessExecutorHelper
import com.github.zkkv.souffleur.interfaces.Cache
import com.github.zkkv.souffleur.interfaces.LanguageModel
import com.github.zkkv.souffleur.structures.TrieCache
import com.intellij.codeInsight.inline.completion.InlineCompletionRequest
import io.mockk.*
import io.mockk.impl.annotations.MockK
import java.io.OutputStream
import java.io.PrintStream
import kotlin.test.*

class OllamaTest {

    // Complex class, it's easier to mock it
    @MockK
    lateinit var request: InlineCompletionRequest

    private lateinit var model: LanguageModel

    private lateinit var cache: Cache

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
        mockkObject(ProcessExecutorHelper)
        cache = TrieCache()
        model = Ollama(cache)
    }

    @AfterTest
    fun teardown() {
        unmockkObject(ProcessExecutorHelper)
    }

    @Test
    fun `prompt returns a non-zero length string with document text in it`() {
        val documentText = "<TEST STRING>"
        val endOffset = 5
        assertTrue(model.prompt(documentText, endOffset).isNotEmpty())
        assertTrue(model.prompt(documentText, endOffset).contains("<TEST"))
    }

    @Test
    fun `suggest succeeds with value not found in cache`() {
        val documentText = "hello world"
        val endOffset = 5
        val response = "response"

        every { request.document.text } returns documentText
        every { request.endOffset } returns endOffset
        every { ProcessExecutorHelper.execute(any()) } returns response

        assertEquals(response, model.suggest(request))
        assertEquals(response, cache.retrieve(model.prompt(documentText, endOffset)))
    }

    @Test
    fun `suggest succeeds with value present in cache`() {
        val documentText = "hello world"
        val endOffset = 5
        val response = "response"

        every { request.document.text } returns documentText
        every { request.endOffset } returns endOffset
        every { ProcessExecutorHelper.execute(any()) } returns response

        val prompt = model.prompt(documentText, endOffset)
        cache.insert(prompt, response)

        assertEquals(response, model.suggest(request))
        assertEquals(response, cache.retrieve(prompt))
    }

    @Test
    fun `process execution fails should return empty string`() {
        val documentText = "hello world"
        val endOffset = 5
        val response = ""

        every { request.document.text } returns documentText
        every { request.endOffset } returns endOffset
        every { ProcessExecutorHelper.execute(any()) } throws RuntimeException()

        // Redirect error stream to avoid clutter in the terminal
        val stderr = System.err
        System.setErr(PrintStream(OutputStream.nullOutputStream()))

        assertEquals(response, model.suggest(request))
        assertFalse(cache.contains(model.prompt(documentText, endOffset)))

        System.setErr(stderr)
    }
}