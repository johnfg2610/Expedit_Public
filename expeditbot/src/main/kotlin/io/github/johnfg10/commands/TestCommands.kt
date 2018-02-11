package io.github.johnfg10.commands

import com.github.kvnxiao.discord.meirei.annotations.Command
import com.github.kvnxiao.discord.meirei.annotations.CommandGroup
import com.github.kvnxiao.discord.meirei.command.CommandContext
import io.github.johnfg10.Expedit
import io.github.johnfg10.Expedit.Companion.discordClient
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

@CommandGroup("test")
class TestCommands {
    @Command(prefix = Expedit.Companion.prefix,aliases = ["ping"], description = "", id = "ping")
    fun ping(context: CommandContext, event: MessageReceivedEvent){
        val nano = event.message.timestamp.nano
        val msg = event.channel.sendMessage("Ping!")
        val msgNano = msg.timestamp.nano

        val nanoTime = msgNano - nano
        val microTime = nanoTime / 1000000
        msg.edit("Ping $microTime ms! ${ if (microTime <= 100){"Thats pretty good!"}
        else if (microTime in 101..250){"Thats not bad!"}
        else if (microTime in 251..500) {"Well its not great but workable!"}
        else {"Well this isnt good!"} } ")
    }


    @Command(prefix = Expedit.Companion.prefix,aliases = ["ping"], description = "", id = "ping")
    fun requestBufferHelper(context: CommandContext, event: MessageReceivedEvent) {

    }
}