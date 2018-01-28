package io.github.johnfg10

import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.util.DiscordException
import sx.blah.discord.util.RequestBuffer

class BotHelper {
    companion object {
        public fun SendMessage(channel: IChannel, message: String){
            RequestBuffer.request {
                try {
                    channel.sendMessage(message)
                } catch (e: DiscordException){
                    Expedit.Companion.logger.error(e.errorMessage)
                }
            }
        }
    }
}