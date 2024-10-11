package com.github.zkkv.souffleur

import com.github.zkkv.souffleur.models.Ollama
import com.intellij.codeInsight.inline.completion.*
import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import io.mockk.*

private const val CM = CodeInsightTestFixture.CARET_MARKER

/*
    Reference (accessed on 2024-10-12):
    https://github.com/JetBrains/intellij-community/blob/785c0c8eedef8233a4b5b3364829447b796d07f4/platform/platform-tests/testSrc/com/intellij/codeInsight/inline/completion/SimpleInlineCompletionTest.kt
 */

/**
 * Mocks the language model behavior and runs plugin tests using IntelliJ SDK.
 *
 * This test suite runs with JUnit 3.
 */
class SouffleurTest : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
        mockkConstructor(Ollama::class)
    }

    override fun tearDown() {
        unmockkConstructor(Ollama::class)  // This is strictly not necessary as it works without it
        super.tearDown()
    }

    override fun runInDispatchThread() = false

    private fun setReturnValuesOfMocks(modelSuggestion: String) {
        every { anyConstructed<Ollama>().suggest(any(InlineCompletionRequest::class)) } returns modelSuggestion
    }

    private fun registerProvider() {
        InlineCompletionHandler.registerTestHandler(Souffleur(), testRootDisposable)
    }

    fun `test inline completion renders on typing`() = myFixture.testInlineCompletion {
        val prefix = "fu"
        val typed = 'n'
        val suggestion = " main(args: List<String>) { }"
        val result = "$prefix$typed$suggestion$CM"

        setReturnValuesOfMocks(suggestion)  // The order of the two is important
        registerProvider()

        init(PlainTextFileType.INSTANCE, "$prefix$CM")  // Initial state of the file

        typeChar(typed)                 // Type a symbol into the editor
        delay()                         // Wait for request to inline completion to finish
        assertInlineRender(suggestion)  // Check what is shown with inlays
        insert()                        // Accept the suggestion
        assertFileContent(result)
        assertInlineHidden()

    }

    fun `test inline completion does not render on direct action call`() = myFixture.testInlineCompletion {
        val suggestion = "Hi, mom"

        setReturnValuesOfMocks(suggestion)
        registerProvider()

        init(PlainTextFileType.INSTANCE, "")

        callInlineCompletion() // Direct action call
        delay()
        assertInlineHidden()
    }

    fun `test inline completion typing multiple characters`() = myFixture.testInlineCompletion {
        init(PlainTextFileType.INSTANCE, CM)

        setReturnValuesOfMocks("his is tutorial")
        registerProvider()  // This can be done once
        typeChar('T')
        delay()
        assertInlineRender("his is tutorial")

        setReturnValuesOfMocks("is is tutorial")
        typeChar('h') // No 'delay' as this update is called instantly
        assertInlineRender("is is tutorial")

        setReturnValuesOfMocks("s is tutorial")
        typeChar('i')
        assertInlineRender("s is tutorial")

        assertFileContent("Thi<caret>")
        insert()
        assertFileContent("This is tutorial<caret>")
        assertInlineHidden()
    }

}