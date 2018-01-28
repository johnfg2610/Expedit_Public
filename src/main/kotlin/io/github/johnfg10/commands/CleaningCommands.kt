package io.github.johnfg10.commands

import com.github.kvnxiao.discord.meirei.annotations.Command
import com.github.kvnxiao.discord.meirei.annotations.CommandGroup
import com.github.kvnxiao.discord.meirei.command.CommandContext
import io.github.johnfg10.Expedit
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.util.RequestBuffer

@CommandGroup("admin.cleaning")
class CleaningCommands {
/*    @Command(prefix = Expedit.Companion.prefix,aliases = ["nuke"], description = "", id = "nuke")
    fun nuke(context: CommandContext, event: MessageReceivedEvent) {
        while (event.channel.fullMessageHistory.count() != 0) {
            for (iMessage in event.channel.getMessageHistory(100)) {
                RequestBuffer.request {
                    iMessage.delete()
                }
            }
        }
    }*/



}