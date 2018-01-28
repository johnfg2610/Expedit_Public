package io.github.johnfg10.commands

import com.github.kvnxiao.discord.meirei.annotations.Command
import com.github.kvnxiao.discord.meirei.annotations.CommandGroup
import com.github.kvnxiao.discord.meirei.annotations.Permissions
import com.github.kvnxiao.discord.meirei.command.CommandContext
import io.github.johnfg10.Expedit
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IUser
import sx.blah.discord.util.DiscordException
import sx.blah.discord.util.MissingPermissionsException

@CommandGroup("admin.manage")
class AdminCommands {
    @Command(id = "banusers", aliases =  ["banusers"], description =  "bans all mentioned users", prefix = Expedit.Companion.prefix)
    @Permissions(reqMention = true)
    fun BanUsers(context: CommandContext, event: MessageReceivedEvent){
        val mentions = event.message.mentions
        event.channel.sendMessage("Banning users!")
        var reason: String = context.args?: "Unknown"

        for(mention in mentions){
            reason = reason.replace("<@${mention.longID}>", "")
            try {
                event.guild.banUser(mention, reason)
            }catch (e: MissingPermissionsException){
                event.channel.sendMessage("The users permissions is equal to or higher then the bots as such the user can not be banned!")
            }

        }
    }

    @Command(id = "kickusers", aliases =  ["kickusers"], description =  "kicks all mentioned users", prefix = Expedit.Companion.prefix)
    @Permissions(reqMention = true)
    fun KickUsers(context: CommandContext, event: MessageReceivedEvent){
        val mentions = event.message.mentions
        event.channel.sendMessage("Kicking users!")
        var reason: String = context.args?: "Unknown"

        for(mention in mentions){
            reason = reason.replace("<@${mention.longID}>", "")
            try {
                event.guild.kickUser(mention, reason)
            }catch (e: MissingPermissionsException){
                event.channel.sendMessage("The users permissions is equal to or higher then the bots as such the user can not be banned!")
            }

        }
    }
}