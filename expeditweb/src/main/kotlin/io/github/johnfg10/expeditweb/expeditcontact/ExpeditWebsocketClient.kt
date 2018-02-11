package io.github.johnfg10.expeditweb.expeditcontact

import io.github.johnfg10.RequestType
import io.github.johnfg10.ResponseBase
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import org.apache.logging.log4j.LogManager
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI
import java.util.*


class ExpeditWebsocketClient(serverUri: URI) : WebSocketClient(serverUri) {

    private val logger = LogManager.getLogger(this.javaClass.name)

    private val callbacks: MutableMap<RequestType, (RequestType, String) -> Unit> = mutableMapOf()

    override fun onOpen(handshakedata: ServerHandshake) {
        println("Handshake data: status code: ${handshakedata.httpStatus} status text: ${handshakedata.httpStatusMessage}")
    }

    override fun onMessage(message: String) {
        //super.onWebsocketMessage(connection, message)
        println(message)
        for (callback in callbacks){
            callback.value(callback.key, message)
        }
    }

    override fun onClose(code: Int, reason: String, remote: Boolean) {
        println("close data: Code: $code, reason: $reason, remote, $remote")
    }

    fun sendMessage(requestType: RequestType): Deferred<Optional<ResponseBase>> {
        return async {
            return@async Optional.empty<ResponseBase>()
        }
    }

    fun sendMessage(requestType: RequestType, callback: (RequestType, String) -> Unit) {
        send(requestType.name)
        callbacks[requestType] = callback
    }

    override fun onError(ex: Exception) {
        logger.error("${ex.message} ${ex.localizedMessage} ${ex.stackTrace}")
    }


}