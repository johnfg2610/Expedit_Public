package io.github.johnfg10.commands

import com.github.kvnxiao.discord.meirei.annotations.Command
import com.github.kvnxiao.discord.meirei.command.CommandContext
import io.github.johnfg10.Expedit
import io.github.johnfg10.database.RulesObj
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

class TrainCommands {
    @Command(id = "train", aliases = ["train"], prefix = Expedit.prefix)
    fun getTrainRoute(context: CommandContext, event: MessageReceivedEvent){

    }
}