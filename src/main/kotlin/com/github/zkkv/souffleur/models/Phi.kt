package com.github.zkkv.souffleur.models

import com.github.zkkv.souffleur.interfaces.Cache

/**
 * Phi LLM.
 */
class Phi(cache: Cache) : Ollama(cache) {
    override val model = "phi3.5"
}