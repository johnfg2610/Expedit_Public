package io.github.johnfg10

import sx.blah.discord.api.internal.json.objects.EmbedObject
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.util.RequestBuffer

fun IChannel.sendMessageReq(msg: String){
    RequestBuffer.request {
        this.sendMessage(msg)
    }
}

fun IChannel.sendMessageReq(msg: EmbedObject){
    RequestBuffer.request {
        this.sendMessage(msg)
    }
}

fun IMessage.deleteMessageReq(){
    RequestBuffer.request {
        this.delete()
    }
}