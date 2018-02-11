package io.github.johnfg10

import com.github.kvnxiao.discord.meirei.d4j.MeireiD4J
import com.natpryce.konfig.Configuration
import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.ConfigurationProperties.Companion.systemProperties
import com.natpryce.konfig.EnvironmentVariables
import com.natpryce.konfig.overriding
import io.github.johnfg10.Expedit.Companion.logger
import io.github.johnfg10.commands.*
import io.github.johnfg10.commands.apis.BitlyCommands
import io.github.johnfg10.database.RulesDatabaseHandler
import io.github.johnfg10.health.HealthManager
import io.github.johnfg10.music.MusicCommands
import io.github.johnfg10.music.MusicManager
import kotlinx.coroutines.experimental.launch
import org.apache.commons.logging.LogFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import sx.blah.discord.api.ClientBuilder
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.util.RequestBuffer
import java.io.File
import kotlin.concurrent.fixedRateTimer
import kotlin.math.log

fun main(args : Array<String>) {
    Expedit()
}

class Expedit{
    companion object {
        lateinit var configuration: Configuration

        val logger: Logger = LoggerFactory.getLogger(Expedit::class.java)
        lateinit var discordClient: IDiscordClient
        const val prefix: String = "+"
        lateinit var rulesDBHandler: RulesDatabaseHandler
        val musicManager: MusicManager = MusicManager()
        val HealthManager: HealthManager = HealthManager()
    }

    init {

        //set configs
        SetupConfigDir()
        Expedit.configuration = SetupConfig()

        //setup rules db info
        Expedit.rulesDBHandler = RulesDatabaseHandler(Expedit.configuration[DatabaseConfig.jdbcConnectionString],
                Expedit.configuration[DatabaseConfig.driver], Expedit.configuration[DatabaseConfig.username],
                Expedit.configuration[DatabaseConfig.password])

        //throws a error if the bot token is empty or null
        if (Expedit.Companion.configuration[BotConfig.token].isEmpty())
            throw IllegalArgumentException("Please fill in token")


        //logs in the bot token
        Expedit.discordClient = ClientBuilder().withToken(Expedit.Companion.configuration[BotConfig.token]).build()

        val meirei = MeireiD4J(discordClient)
        //main commands
        meirei.addAnnotatedCommands(TestCommands(), AdminCommands(), MiscCommands(), RulesCommands(), CleaningCommands(), MusicCommands())
        //apis
        meirei.addAnnotatedCommands(BitlyCommands())

        //launch the cmd input system
        launch {
            while (true){
                try{
                    val cmdInput = readLine()
                    when(cmdInput){
                        "stop" -> {
                            discordClient.logout()
                        }
                        "start" -> {
                            discordClient.login()
                        }
                        "reconnect" -> {
                            discordClient.logout()
                            discordClient.login()
                        }
                        "exit" -> {
                            discordClient.logout()
                            System.exit(0)
                        }
                        "requesthelper" -> {
                            logger.info(
                                    "Request helper information: Incomplete request count: ${RequestBuffer.getIncompleteRequestCount()}"
                            )

                        }
                    }
                    if (cmdInput?.startsWith("say") == true){
                        val cmdSplit = cmdInput.split(" ")
                        val channelId = cmdSplit[1].toLong()
                        val channel = discordClient.getChannelByID(channelId)
                        val final = cmdInput.replace("say", "").replace(channelId.toString(), "")
                        channel.sendMessage(final)
                    }
                    if (cmdInput?.startsWith("playing") == true){
                        val final = cmdInput.replace("playing", "")
                        discordClient.changePlayingText(final)
                    }
                    if (cmdInput?.startsWith("status") == true){
                        val cmdSplit = cmdInput.split(" ")
                        when (cmdSplit[1]){
                            "dnd" -> discordClient.dnd()
                            "idle" -> discordClient.idle()
                            "online" -> discordClient.online()
                            "invis" -> discordClient.invisible()
                        }
                    }
                }catch (e: Exception){}
            }
        }

        discordClient.dispatcher.registerListener(io.github.johnfg10.handlers.MessageHandler())
        discordClient.login()

        //lets make sure the client is always connected
        fixedRateTimer("heartbeat-timer", initialDelay = 1800000, period = 1200000){
            launch {
                if (!discordClient.isLoggedIn){
                    discordClient.login()
                }
            }
        }
    }

    private fun SetupConfigDir(){
        if (!File("./configs").exists()){
            File("./configs").mkdirs()
        }
    }

    private fun SetupConfig() : Configuration {
        val configFile = File("./configs/config.properties")
        println(configFile.absolutePath)
        if (!configFile.exists()){
            configFile.createNewFile()
            configFile.writeText(Expedit::class.java.getResource("/config.properties").readText())
        }

        return systemProperties() overriding
                EnvironmentVariables() overriding
                ConfigurationProperties.fromFile(File("configs/config.properties"))
    }
}