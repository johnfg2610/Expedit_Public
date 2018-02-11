package io.github.johnfg10.commands

import com.github.kvnxiao.discord.meirei.annotations.Command
import com.github.kvnxiao.discord.meirei.annotations.CommandGroup
import com.github.kvnxiao.discord.meirei.annotations.Permissions
import com.github.kvnxiao.discord.meirei.command.CommandContext
import io.github.johnfg10.BotHelper
import io.github.johnfg10.Expedit
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IUser
import sx.blah.discord.util.DiscordException
import sx.blah.discord.util.MissingPermissionsException

@CommandGroup("admin.manage")
class AdminCommands {
    @Command(id = "banusers", aliases =  ["banusers", "banuser", "ban"], description =  "bans all mentioned users", prefix = Expedit.Companion.prefix)
    fun BanUsers(context: CommandContext, event: MessageReceivedEvent){
        val mentions = event.message.mentions

        if (mentions.isEmpty()) {
            BotHelper.SendMessage(event.channel, "This command requires users to be tagged as arguments!")
            return
        }

        var reason = context.args

        for (mention in mentions){
            if (reason != null) {
                reason = reason.replace(Regex("[<@][\\S]*[>]"), "")
                BotHelper.BanUser(event.guild, mention, reason)
            } else {
                BotHelper.BanUser(event.guild, mention)
            }
        }

        BotHelper.SendMessage(event.channel, "Users banned!")
    }

    @Command(id = "kickusers", aliases =  ["kickusers"], description =  "kicks all mentioned users", prefix = Expedit.Companion.prefix)
    fun KickUsers(context: CommandContext, event: MessageReceivedEvent){
        val mentions = event.message.mentions

        if (mentions.isEmpty()){
            BotHelper.SendMessage(event.channel, "This command requires users to be tagged as arguments!")
            return
        }

        var reason = context.args

        for (mention in mentions){
            if (reason != null) {
                reason = reason.replace(Regex("[<@][\\S]*[>]"), "")
                BotHelper.KickUser(event.guild, mention, reason)
            } else {
                BotHelper.KickUser(event.guild, mention)
            }
        }

        BotHelper.SendMessage(event.channel, "Users banned!")
    }
}