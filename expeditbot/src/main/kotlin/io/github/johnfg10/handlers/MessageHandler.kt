package io.github.johnfg10.handlers

import io.github.johnfg10.Expedit
import org.apache.commons.validator.routines.UrlValidator
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageSendEvent

class MessageHandler {

    @EventSubscriber
    fun onMessageEvent(event: MessageReceivedEvent){
        if (event.message.isDeleted || event.message.isSystemMessage)
            return

        val msgContent = event.message.content

        if (Expedit.rulesDBHandler.ignoreChannels.contains(event.channel.longID) && Expedit.rulesDBHandler.ignoreDefaults.contains(event.guild.longID))
            return

        if (Expedit.rulesDBHandler.channelCache.get(event.channel.longID) == null) {
            Expedit.rulesDBHandler.ignoreChannels.add(event.channel.longID)
        }

        if (Expedit.rulesDBHandler.defaultCache.get(event.channel.longID) == null) {
            Expedit.rulesDBHandler.ignoreDefaults.add(event.channel.longID)
        }

        if (Expedit.rulesDBHandler.channelCache.get(event.channel.longID)?.isLinksEnabled == false || Expedit.rulesDBHandler.defaultCache[event.guild.longID]?.isLinksEnabled == false){
            println("checking links")
            if (msgContent.contains(Regex("www.\\S+.\\S+"))||
                    msgContent.contains(".gg") || msgContent.contains(".com", ignoreCase = true)||
                    msgContent.contains(".co", ignoreCase = true) || msgContent.contains(".de", ignoreCase = true)||
                    msgContent.contains(".cn", ignoreCase = true)||msgContent.contains(".net", ignoreCase = true)||
                    msgContent.contains(".uk", ignoreCase = true)||msgContent.contains(".org", ignoreCase = true)||
                    msgContent.contains(".info", ignoreCase = true)||msgContent.contains(".nl", ignoreCase = true)||
                    msgContent.contains(".eu", ignoreCase = true)||msgContent.contains(".ru", ignoreCase = true)
                    ||msgContent.contains(".ly", ignoreCase = true)||msgContent.contains("http:", ignoreCase = true)
                    ||msgContent.contains("https:", ignoreCase = true)){
                event.message.delete()
            }
        }

        if (Expedit.rulesDBHandler.channelCache.get(event.channel.longID)?.isAttachmentsEnabled == false || Expedit.rulesDBHandler.defaultCache[event.guild.longID]?.isAttachmentsEnabled == false) {
            if (!event.message.attachments.isEmpty()){
                event.message.delete()
            }
        }
    }
}