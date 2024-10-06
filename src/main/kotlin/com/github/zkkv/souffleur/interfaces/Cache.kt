package com.github.zkkv.souffleur.interfaces

interface Cache {
    fun contains(key: String): Boolean

    fun retrieve(key: String): String?

    fun insert(key: String, value: String)
}