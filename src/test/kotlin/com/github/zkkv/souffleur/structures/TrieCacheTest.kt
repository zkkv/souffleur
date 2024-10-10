package com.github.zkkv.souffleur.structures

import com.github.zkkv.souffleur.interfaces.Cache
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test
import kotlin.test.BeforeTest

class TrieCacheTest {

    private lateinit var sut: Cache

    @BeforeTest
    fun `setup TrieCache instance`() {
        sut = TrieCache()
    }

    @Test
    fun `should not contain empty string in empty TrieCache`() {
        assertFalse(sut.contains(""))
    }

    @Test
    fun `should return null when retrieving empty string from empty TrieCache`() {
        assertNull(sut.retrieve(""))
    }

    @Test
    fun `should contain key after inserting it`() {
        sut.insert("A", "B")
        assertTrue(sut.contains("A"))
    }

    @Test
    fun `should retrieve value after inserting it with a key`() {
        sut.insert("A", "B")
        assertEquals("B", sut.retrieve("A"))
    }

    @Test
    fun `should not contain different key after inserting another`() {
        sut.insert("A", "B")
        assertFalse(sut.contains("C"))
    }

    @Test
    fun `should return null for retrieving different key after inserting another`() {
        sut.insert("A", "B")
        assertNull(sut.retrieve("C"))
    }

    @Test
    fun `should insert multiple keys with same prefix and retrieve correctly`() {
        sut.insert("Ab", "B")
        sut.insert("Ac", "C")

        assertFalse(sut.contains("A"))
        assertEquals("B", sut.retrieve("Ab"))
        assertEquals("C", sut.retrieve("Ac"))
    }

    @Test
    fun `should insert multiple keys with different prefixes and retrieve correctly`() {
        sut.insert("Ab", "B")
        sut.insert("Cd", "D")

        assertEquals("B", sut.retrieve("Ab"))
        assertEquals("D", sut.retrieve("Cd"))
    }

}