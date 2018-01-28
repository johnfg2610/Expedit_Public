package io.github.johnfg10.music

import com.github.kvnxiao.discord.meirei.annotations.Command
import com.github.kvnxiao.discord.meirei.annotations.CommandGroup
import com.github.kvnxiao.discord.meirei.command.CommandContext
import io.github.johnfg10.Expedit
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.obj.IVoiceChannel

@CommandGroup("user.music")
class MusicCommands {
    @Command(id = "loadurl", aliases = ["loadsong"], prefix = Expedit.prefix)
    fun LoadUrl(context: CommandContext, event: MessageReceivedEvent){
        Expedit.musicManager.loadUrl(event.channel, context.args ?: return)
    }

    @Command(id = "joinchannel", aliases = ["join"], prefix = Expedit.prefix)
    fun JoinChannel(context: CommandContext, event: MessageReceivedEvent){
        for (connectedVoiceChannel in Expedit.discordClient.connectedVoiceChannels) {
            if (connectedVoiceChannel.guild == event.guild){
                event.channel.sendMessage("The bot can only be in one channel per guild!")
                return
            }
        }

        if ( event.author.getVoiceStateForGuild(event.guild).channel != null){
            event.author.getVoiceStateForGuild(event.guild).channel.join()
        }
    }

    @Command(id = "leavechannel", aliases = ["leave"], prefix = Expedit.prefix)
    fun LeaveChannel(context: CommandContext, event: MessageReceivedEvent){
        if ( event.author.getVoiceStateForGuild(event.guild).channel != null){
            event.author.getVoiceStateForGuild(event.guild).channel.join()
        }
    }

    @Command(id = "skiptrack", aliases = ["skip"], prefix = Expedit.prefix)
    fun SkipTrack(context: CommandContext, event: MessageReceivedEvent){
        Expedit.musicManager.skipTrack(event.guild)
    }
}