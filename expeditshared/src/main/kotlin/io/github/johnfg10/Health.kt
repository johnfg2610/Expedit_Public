package io.github.johnfg10

data class Health(
        val maxMemory: Long,
        val freeMemory: Long,
        val totalMemory: Long,
        val loginStatus: Boolean,
        val isReady: Boolean)