package io.github.johnfg10

import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IUser
import sx.blah.discord.util.DiscordException
import sx.blah.discord.util.MessageHistory
import sx.blah.discord.util.RateLimitException
import sx.blah.discord.util.RequestBuffer

class BotHelper {
    companion object {
        fun SendMessage(channel: IChannel, message: String){
            RequestBuffer.request {
                channel.sendMessage(message)
            }
        }

        fun DeleteMessages(messageHistory: MessageHistory){
            for (iMessage in messageHistory) {
                DeleteMessage(iMessage)
            }
        }

        fun DeleteMessages(messageHistory: MessageHistory, user: IUser){
            for (iMessage in messageHistory) {
                if (iMessage.author == user)
                    DeleteMessage(iMessage)
            }
        }

        fun DeleteMessages(messageHistory: MessageHistory, users: Array<out IUser>){
            for (iMessage in messageHistory) {
                if (users.contains(iMessage.author))
                    DeleteMessage(iMessage)
            }
        }

        fun DeleteMessage(message: IMessage){
            RequestBuffer.request {
                message.delete()
            }
        }

        fun BanUser(guild: IGuild, user: IUser){
            RequestBuffer.request {
                guild.banUser(user)
            }
        }

        fun BanUser(guild: IGuild, user: IUser, reason: String){
            RequestBuffer.request {
                guild.banUser(user, reason)
            }
        }

        fun KickUser(guild: IGuild, user: IUser){
            RequestBuffer.request {
                guild.kickUser(user)
            }
        }

        fun KickUser(guild: IGuild, user: IUser, reason: String){
            RequestBuffer.request {
                guild.kickUser(user, reason)
            }
        }
    }
}