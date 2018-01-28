package io.github.johnfg10.nationalrail.authenticate

data class Authenticate(val username: String, val roles: List<Role>, val token: String)