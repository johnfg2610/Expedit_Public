package io.github.johnfg10.expeditweb.expeditcontact

import com.beust.klaxon.Klaxon
import io.github.johnfg10.ExpeditExternConfiguration
import io.github.johnfg10.RequestType
import io.github.johnfg10.ResponseBase
import kotlinx.coroutines.experimental.*
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URI
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.log


class ExpeditContainer(jarLocation: String, expeditExternConfigFile: String) {

    companion object {
        val logger = LoggerFactory.getLogger(this::class.java)
    }

    private val process: Process

    //private val expeditWebsocketClient: ExpeditWebsocketClient

    init {
        process = jarSetup(jarLocation, mapOf(Pair("expeditExternPath", expeditExternConfigFile)))

/*        expeditWebsocketClient = setupWebsocket(expeditExternConfigFile)
        println("Is open: ${expeditWebsocketClient.isOpen} Is closed: ${expeditWebsocketClient.isClosed}")*/
    }

/*    fun sendMessage(requestType: RequestType) : Deferred<Optional<ResponseBase>> {
        if (!process.isAlive)
            throw IllegalStateException("Process has already been killed")
        return expeditWebsocketClient.sendMessage(requestType)
    }*/

    private fun getProcessHandle() : ProcessHandle {
        return process.toHandle()
    }

    private fun jarSetup(jarLocation: String, jarArgs: Map<String, String> = mapOf()) : Process {
        val jarFile = File(jarLocation)
        if (jarFile.exists() && jarFile.isFile && jarFile.extension == "jar") {
            val builder = ProcessBuilder("java", "-jar", jarFile.path)
            builder.inheritIO()
            builder.environment().putAll(jarArgs)
            return builder.start()
        }else {
            throw IllegalArgumentException("The file either does not exist, is not a file or does not have the .jar extension. $jarFile")
        }
    }

/*    private fun setupWebsocket(expeditExternConfigFile: String): ExpeditWebsocketClient {
        val file = File(expeditExternConfigFile)
        return runBlocking {
            delay(10, TimeUnit.SECONDS)
            repeat(3){
                if (file.exists() && file.isFile && file.extension == "json"){
                    val expeditNullable = Klaxon().parse<ExpeditExternConfiguration>(File(expeditExternConfigFile))
                    if (expeditNullable != null){
                        if (!expeditNullable.isEnabled)
                            throw IllegalStateException("Web socket support is disabled in the expedit configuration")
                        else{
                            println("socket port: ${expeditNullable.socketPort}")
                            val expeditWebSocket = ExpeditWebsocketClient(URI("ws://localhost:${expeditNullable.socketPort}"))
                            expeditWebSocket.connectBlocking()
                            println("websocket connected!")

                            return@runBlocking expeditWebSocket
                        }
                    }
                }
                delay(30, TimeUnit.SECONDS)
            }
            throw IllegalStateException("Expedits extern configuration could not be loaded")
        }
    }*/
}