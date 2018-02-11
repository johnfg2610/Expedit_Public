package io.github.johnfg10.commands

import com.github.kvnxiao.discord.meirei.annotations.Command
import com.github.kvnxiao.discord.meirei.annotations.CommandGroup
import com.github.kvnxiao.discord.meirei.command.CommandContext
import io.github.johnfg10.Expedit
import sx.blah.discord.api.DiscordStatus
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.impl.obj.Embed
import sx.blah.discord.util.EmbedBuilder
import sx.blah.discord.util.RequestBuffer

@CommandGroup("misc.commands")
class MiscCommands {

    @Command(id = "help", aliases = ["help", "halp", "hepp"], prefix = Expedit.prefix)
    fun help(context: CommandContext, event: MessageReceivedEvent){
        if (context.readOnlyCommandRegistry == null) {
            event.channel.sendMessage("Command reg was null!")
        }

/*        if (context.readOnlyCommandRegistry != null){
            val commands = context.readOnlyCommandRegistry!!.getAllCommands()
            var embedBuilder = EmbedBuilder()

            for (command in commands){
                if (embedBuilder.fieldCount == EmbedBuilder.FIELD_COUNT_LIMIT){
                    RequestBuffer.request { event.channel.sendMessage(embedBuilder.build()) }
                    embedBuilder = EmbedBuilder()
                }

                val aliases = ""

                for (ali in command.commandProperties.aliases ){
                    aliases.plus(ali)
                }

                embedBuilder.appendField(aliases, command.commandProperties.description, false)
            }

            if (embedBuilder.fieldCount > 0 ){
                RequestBuffer.request { event.channel.sendMessage(embedBuilder.build()) }
            }
        }else{
            RequestBuffer.request { event.channel.sendMessage("Command registry was null") }
        }*/
    }

    @Command(id = "getstatus", aliases = ["status"], prefix = Expedit.prefix)
    fun getStaus(context: CommandContext, event: MessageReceivedEvent){
        val embededBuild = EmbedBuilder()
        embededBuild.appendField("api response time: ", "Day: ${DiscordStatus.getAPIResponseTimeForDay()} Week: ${DiscordStatus.getAPIResponseTimeForWeek()} Month: ${DiscordStatus.getAPIResponseTimeForMonth()}", true)
        for (upcomingMaintenance in DiscordStatus.getUpcomingMaintenances()) {
            embededBuild.appendField("Maintenance in progress: ${upcomingMaintenance.id}", "Description: ${upcomingMaintenance.description} Ends at: ${upcomingMaintenance.end}", true)
        }
        event.channel.sendMessage(embededBuild.build())
    }
}