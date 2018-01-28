package io.github.johnfg10

import com.github.kvnxiao.discord.meirei.d4j.MeireiD4J
import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import io.github.johnfg10.commands.*
import io.github.johnfg10.database.RulesDatabaseHandler
import io.github.johnfg10.music.MusicCommands
import io.github.johnfg10.music.MusicManager
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import sx.blah.discord.api.ClientBuilder
import sx.blah.discord.api.IDiscordClient
import java.io.File
import kotlin.concurrent.fixedRateTimer

fun main(args: Array<String>){
    //pass flow of control off to Expedit
    Expedit()
}

class Expedit{
    companion object {
        lateinit var botConfig: BotConfig
        lateinit var databaseConfig: DatabaseConfig
        val logger: Logger = LoggerFactory.getLogger(Expedit::class.java)
        lateinit var discordClient: IDiscordClient
        const val prefix: String = "+"
        lateinit var rulesDBHandler: RulesDatabaseHandler
        val musicManager: MusicManager = MusicManager()
    }

    init {
        SetupConfigDir()

        val configPair = SetupConfig()
        Expedit.Companion.botConfig = configPair.first
        Expedit.Companion.databaseConfig = configPair.second

        Expedit.rulesDBHandler = RulesDatabaseHandler(Expedit.databaseConfig.jdbcConnectionString,
                Expedit.databaseConfig.driver, Expedit.databaseConfig.username, Expedit.databaseConfig.password)

        if (Expedit.Companion.botConfig.token == "")
            throw IllegalArgumentException("Please fill in token")

        Expedit.discordClient = ClientBuilder().withToken(Expedit.Companion.botConfig.token).build()

        val meirei = MeireiD4J(discordClient)
        meirei.addAnnotatedCommands(TestCommands())
        meirei.addAnnotatedCommands(AdminCommands())
        meirei.addAnnotatedCommands(MiscCommands())
        meirei.addAnnotatedCommands(RulesCommands())
        meirei.addAnnotatedCommands(CleaningCommands())
        meirei.addAnnotatedCommands(MusicCommands())

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
            async{
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

    private fun SetupConfig() : Pair<BotConfig, DatabaseConfig> {
        val configFile = File("./configs/config.hocon")
        if (!configFile.exists()){
            configFile.createNewFile()
            configFile.writeText(Expedit::class.java.getResource("/config.hocon").readText())
        }

        val config = ConfigFactory.parseFile(configFile)

        val botConfig: BotConfig = config.extract("bot")
        val databaseConfig: DatabaseConfig = config.extract("database")

        return Pair(botConfig, databaseConfig)
    }
}