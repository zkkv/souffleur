package com.github.zkkv.souffleur.structures

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TrieCacheTest {

    private lateinit var sut: TrieCache;

    @BeforeEach
    fun setup() {
        sut = TrieCache()
    }

    @Test
    fun containsEmpty() {
        assertFalse(sut.contains(""))
    }

    @Test
    fun retrieveEmpty() {
        assertNull(sut.retrieve(""))
    }

    @Test
    fun insertAndContainsSame() {
        sut.insert("A", "B")
        assertTrue(sut.contains("A"))
    }

    @Test
    fun insertAndRetrieveSame() {
        sut.insert("A", "B")
        assertEquals("B", sut.retrieve("A"))
    }

    @Test
    fun insertAndContainsDifferent() {
        sut.insert("A", "B")
        assertFalse(sut.contains("C"))
    }

    @Test
    fun insertAndRetrieveDifferent() {
        sut.insert("A", "B")
        assertNull(sut.retrieve("C"))
    }

    @Test
    fun insertMultipleSamePrefix() {
        sut.insert("Ab", "B")
        sut.insert("Ac", "C")

        assertFalse(sut.contains("A"))
        assertEquals("B", sut.retrieve("Ab"))
        assertEquals("C", sut.retrieve("Ac"))
    }

    @Test
    fun insertMultipleDifferentPrefixes() {
        sut.insert("Ab", "B")
        sut.insert("Cd", "D")

        assertEquals("B", sut.retrieve("Ab"))
        assertEquals("D", sut.retrieve("Cd"))
    }

}