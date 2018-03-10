package io.github.johnfg10.modules.music.commands

import io.github.johnfg10.Expedit
import io.github.johnfg10.api.SearchYoutube
import io.github.johnfg10.command.Command
import io.github.johnfg10.command.CommandArg
import io.github.johnfg10.command.CommandArgumentType
import io.github.johnfg10.command.CommandController
import io.github.johnfg10.command.flags.CommandArgument
import io.github.johnfg10.command.flags.CommandFlag
import io.github.johnfg10.modules.music.GuildMusicManager
import io.github.johnfg10.sendMessageReq
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IUser

@CommandController
class MusicCommands {

    @Command("playyt", ["playyt", "playyoutube"])
    fun playYoutube(
            @CommandArg(CommandArgumentType.Channel) channel: IChannel,
            @CommandArg(CommandArgumentType.Guild) guild: IGuild,
            @CommandArg(CommandArgumentType.Author) author: IUser,
            @CommandArg(CommandArgumentType.Arguments) args: List<CommandArgument>,
            @CommandArg(CommandArgumentType.Mentions) mentions: List<IUser>,
            @CommandArg(CommandArgumentType.Message) msg: IMessage,
            @CommandArg(CommandArgumentType.RemainingMessage) remainingMessage: String
    ){
        if (remainingMessage.isBlank()){
            channel.sendMessage("This command requires a search parameter")
            return
        }

        val searchResult = SearchYoutube(remainingMessage).getResults(1).first()
        val argg = args.toMutableList()
        argg.add(CommandArgument(CommandFlag("play"), searchResult.url)  )

        play(channel, guild, author, argg, mentions, msg)
    }

    @Command("play", ["play"])
    fun play(
            @CommandArg(CommandArgumentType.Channel) channel: IChannel,
             @CommandArg(CommandArgumentType.Guild) guild: IGuild,
             @CommandArg(CommandArgumentType.Author) author: IUser,
             @CommandArg(CommandArgumentType.Arguments) args: List<CommandArgument>,
             @CommandArg(CommandArgumentType.Mentions) mentions: List<IUser>,
             @CommandArg(CommandArgumentType.Message) msg: IMessage
    ){
        val voiceChannel = author.getVoiceStateForGuild(guild).channel
        if (voiceChannel == null){
            channel.sendMessage("You must be in a voice channel")
            return
        }

        val playVal = args.firstOrNull{ it.flag.flag == "play" }

        if (playVal == null || playVal.value.isBlank()) {
            channel.sendMessageReq("You must include a valid url --play=www.example.com")
            return
        }else{
            voiceChannel.join()
            Expedit.musicManager.loadAndPlay(channel, playVal.value)
        }
    }

    @Command("stop", ["stop"])
    fun stop(
            @CommandArg(CommandArgumentType.Channel) channel: IChannel,
             @CommandArg(CommandArgumentType.Guild) guild: IGuild,
             @CommandArg(CommandArgumentType.Author) author: IUser,
             @CommandArg(CommandArgumentType.Arguments) args: List<CommandArgument>,
             @CommandArg(CommandArgumentType.Mentions) mentions: List<IUser>,
             @CommandArg(CommandArgumentType.Message) msg: IMessage
    ){
        Expedit.musicManager.getGuildAudioPlayer(guild).scheduler.queue.forEach { Expedit.musicManager.getGuildAudioPlayer(guild).scheduler.queue.remove(it) }
        guild.connectedVoiceChannel.leave()
    }

    @Command("vol", ["vol"])
    fun volControl(
            @CommandArg(CommandArgumentType.Channel) channel: IChannel,
            @CommandArg(CommandArgumentType.Guild) guild: IGuild,
            @CommandArg(CommandArgumentType.Author) author: IUser,
            @CommandArg(CommandArgumentType.Arguments) args: List<CommandArgument>,
            @CommandArg(CommandArgumentType.Mentions) mentions: List<IUser>,
            @CommandArg(CommandArgumentType.Message) msg: IMessage
    ){
        val volume = args.firstOrNull { it.flag.flag == "vol" }?.value?.toIntOrNull()
        if (volume == null){
            channel.sendMessage("Must contain a volume argument -vol=50 and must be a numeric value")
            return
        }
        Expedit.musicManager.getGuildAudioPlayer(guild).audioPlayer.volume = volume
    }

    @Command("skip", ["skip"])
    fun skip(
            @CommandArg(CommandArgumentType.Channel) channel: IChannel,
            @CommandArg(CommandArgumentType.Guild) guild: IGuild,
            @CommandArg(CommandArgumentType.Author) author: IUser,
            @CommandArg(CommandArgumentType.Mentions) mentions: List<IUser>,
            @CommandArg(CommandArgumentType.Message) msg: IMessage
    ){
        Expedit.musicManager.getGuildAudioPlayer(guild).scheduler.nextTrack()
    }
}