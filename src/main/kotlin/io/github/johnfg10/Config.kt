package io.github.johnfg10

data class BotConfig(val token: String, val prefix: String)

data class DatabaseConfig(val jdbcConnectionString: String, val driver: String, val username: String, val password: String)