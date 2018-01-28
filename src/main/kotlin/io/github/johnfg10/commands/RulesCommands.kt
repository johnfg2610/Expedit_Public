package io.github.johnfg10.commands

import com.github.kvnxiao.discord.meirei.annotations.Command
import com.github.kvnxiao.discord.meirei.annotations.CommandGroup
import com.github.kvnxiao.discord.meirei.annotations.Permissions
import com.github.kvnxiao.discord.meirei.command.CommandContext
import io.github.johnfg10.Expedit
import io.github.johnfg10.database.RulesDatabase
import io.github.johnfg10.database.RulesObj
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.util.EmbedBuilder

@CommandGroup("admin.rules")
class RulesCommands {

    @Command(id = "setlinkrule", aliases = ["linkrule="], prefix = Expedit.prefix)
    fun setChannelEnableLink(context: CommandContext, event: MessageReceivedEvent){
        val enableLinks = context.args?.toBoolean() ?: false
        Expedit.rulesDBHandler.SetChannelRules(event.channel.longID, RulesObj(enableLinks, null))
    }

    @Command(id = "setattachmentrule", aliases = ["attachrule="], prefix = Expedit.prefix)
    fun setChannelAttachmentRule(context: CommandContext, event: MessageReceivedEvent){
        val enableAttachments = context.args?.toBoolean() ?: false
        Expedit.rulesDBHandler.SetChannelRules(event.channel.longID, RulesObj(null, enableAttachments))
    }

    @Command(id = "getchannelrule", aliases = ["channelrules"], prefix = Expedit.prefix)
    fun getChannelRules(context: CommandContext, event: MessageReceivedEvent){
        val channelCache = Expedit.rulesDBHandler.channelCache.get(event.channel.longID)
        if (channelCache != null){
            val builder: EmbedBuilder = EmbedBuilder()
            builder.appendField("Are links enabled: ", channelCache.isLinksEnabled.toString(), true)
            builder.appendField("Are attachments enabled: ", channelCache.isAttachmentsEnabled.toString(), true)
            event.channel.sendMessage(builder.build())
        }else{
            event.channel.sendMessage("No settings are saved")
        }
    }

    @Command(id = "flushcache", aliases = ["flushcache"], prefix = Expedit.prefix)
    @Permissions(reqBotOwner = true)
    fun clearCache(context: CommandContext, event: MessageReceivedEvent){
        when(context.args){
            "default" -> {
                Expedit.rulesDBHandler.defaultCache.invalidateAll()
                event.channel.sendMessage("Default flush requested")
            }
            "channel" -> {
                Expedit.rulesDBHandler.channelCache.invalidateAll()
                event.channel.sendMessage("Channel flush requested")
            }
        }
    }
}