package io.github.johnfg10.health

class HealthManager {
    val ramHealthManager: RamHealthManager

    init {
        ramHealthManager = RamHealthManager()
    }
}