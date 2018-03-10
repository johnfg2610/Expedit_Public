package io.github.johnfg10

import io.github.johnfg10.Expedit.Companion.baseFolder
import io.github.johnfg10.Expedit.Companion.client
import io.github.johnfg10.Expedit.Companion.expeditConfig
import io.github.johnfg10.Expedit.Companion.expeditRabbitMQ
import io.github.johnfg10.Expedit.Companion.kdiscord
import io.github.johnfg10.Expedit.Companion.logger
import io.github.johnfg10.api.Search
import io.github.johnfg10.api.SearchImage
import io.github.johnfg10.api.SearchYoutube
import io.github.johnfg10.config.DockerConfigProvider
import io.github.johnfg10.config.EnviormentConfigProvider
import io.github.johnfg10.config.JsonConfigProvider
import io.github.johnfg10.modules.music.MusicManager
import io.github.johnfg10.rabbitmq.ExpeditRabbitMQ
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import sx.blah.discord.api.ClientBuilder
import sx.blah.discord.api.IDiscordClient
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

fun main(args: Array<String>) {

    if(!baseFolder.exists())
        baseFolder.mkdirs()

    Expedit.expeditConfig = loadConfig()
    if (Expedit.expeditConfig.discordApiToken.isEmpty())
        throw IllegalStateException("Discord api token must not be null")

    try {
        expeditRabbitMQ = ExpeditRabbitMQ(expeditConfig.rabbitMQ)
    }catch (e: Exception){
        logger.exception(e)
    }

    //sexpeditRabbitMQ = ExpeditRabbitMQ(expeditConfig.rabbitMQ)

    client = createClient(Expedit.expeditConfig.discordApiToken, false)

    client!!.dispatcher.registerListener(Expedit())

    kdiscord = KDiscord4J(client!!, File(baseFolder, "permissions.json"),"-")

    launch {
        while (true){
            val readLine = readLine()
            if (readLine != null){
                val splitRead = readLine.split(" ")
                if (splitRead[0].contains(Regex("[^\\\\d]"))){
                    client!!.getChannelByID(splitRead[0].toLong()).sendMessage(readLine.replace(splitRead[0], ""))
                }
            }
        }
    }

    launch {
        fun wait(){
            if (client!!.isReady){
            }else{
                runBlocking { delay(5, TimeUnit.SECONDS) }
                wait()
            }
        }
        wait()
    }

    client!!.login()


}

fun createClient(token: String, login: Boolean): IDiscordClient {
    val clientBuilder = ClientBuilder()
    clientBuilder.withToken(token)
    return if (login) {
        clientBuilder.login()
    } else {
        clientBuilder.build()
    }
}

fun loadConfig(): ExpeditConfig {
    if (isDockerContainer()){
        try {
            return DockerConfigProvider().getConfig()
        } catch (e: Exception){
            logger.exception(e)
        }
    }
    try {
        if (System.getenv().containsKey("DISCORD_TOKEN")){
            println("using enviormental loader")
            return EnviormentConfigProvider().getConfig()
        }
    } catch (e: Exception){
        logger.exception(e)
    }

    return JsonConfigProvider(File(baseFolder, "expeditconfig.json")).getConfig()
}

class Expedit{
    companion object {
        lateinit var expeditConfig: ExpeditConfig
        var kdiscord: KDiscord4J? = null
        var client: IDiscordClient? = null
        val baseFolder = File("./data")
        var expeditRabbitMQ: ExpeditRabbitMQ? = null
        val musicManager: MusicManager = MusicManager()
        val logger: Logger = LoggerFactory.getLogger(Expedit::class.java)
    }
}

fun isDockerContainer() : Boolean {
    if (File("/.dockerenv").exists())
        return true
    if (File("/.dockerinit").exists())
        return true
    return false
}