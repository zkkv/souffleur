package com.github.zkkv.souffleur.structures

import com.github.zkkv.souffleur.interfaces.Cache

class TrieCache : Cache {
    override fun contains(key: String): Boolean {
        var currentNode: Node = root
        for (c in key) {
            currentNode = currentNode.children[c] ?: return false
        }
        return currentNode.isEnd
    }

    override fun retrieve(key: String): String? {
        var currentNode: Node = root
        for (c in key) {
            currentNode = currentNode.children[c] ?: return null
        }
        return currentNode.value
    }

    override fun insert(key: String, value: String) {
        var currentNode: Node = root
        for (c in key) {
            currentNode = currentNode.children.getOrPut(c) { Node(value=null) }
        }
        currentNode.value = value
        currentNode.isEnd = true
    }

    private val root: Node = Node(value=null)

    private data class Node(
        var value: String?, /* Associated value */
        val children: MutableMap<Char, Node> = HashMap(),
        var isEnd: Boolean = false,
    )
}