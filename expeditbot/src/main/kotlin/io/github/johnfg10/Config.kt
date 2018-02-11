package io.github.johnfg10

import com.natpryce.konfig.PropertyGroup
import com.natpryce.konfig.getValue
import com.natpryce.konfig.stringType

object BotConfig : PropertyGroup(){
    val token by stringType
    val prefix by stringType
}


object DatabaseConfig : PropertyGroup(){
    val jdbcConnectionString by stringType
    val driver by stringType
    val username by stringType
    val password by stringType
}

object TokenConfig : PropertyGroup(){
    val bitlyToken by stringType
    val metOfficeToken by stringType
}