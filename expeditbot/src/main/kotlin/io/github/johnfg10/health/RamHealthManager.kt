package io.github.johnfg10.health

class RamHealthManager {
    fun getRuntime(): Runtime {
        return Runtime.getRuntime()
    }

    public fun getTotalMemory(): Long {
        return getRuntime().totalMemory()
    }

    public fun getFreeMemory(): Long {
        return getRuntime().freeMemory()
    }

    public fun getMaxMemory(): Long {
        return getRuntime().maxMemory()
    }
}