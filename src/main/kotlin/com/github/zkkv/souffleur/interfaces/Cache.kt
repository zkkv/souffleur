package com.github.zkkv.souffleur.interfaces

/**
 * Cache that stores keys and values, both of type String.
 */
interface Cache {

    /**
     * Returns true if key is in the cache, false otherwise.
     *
     * @param key key used to search in the cache
     * @return true if key is contained, false otherwise
     */
    fun contains(key: String): Boolean

    /**
     * Retrieves value associated with the key.
     *
     * @param key key used to search in the cache
     * @return value associated with the given key, or null if it's key is not present
     */
    fun retrieve(key: String): String?

    /**
     * Adds key and associated value to the cache.
     *
     * @param key key to place in the cache
     * @param value value that is associated with the passed key
     */
    fun insert(key: String, value: String)
}