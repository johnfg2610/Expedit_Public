package io.github.johnfg10.music

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import io.github.johnfg10.BotHelper
import io.github.johnfg10.music.audio.GuildMusicManager
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IGuild


class MusicManager {
    val musicManagers: MutableMap<Long, GuildMusicManager> = mutableMapOf()
    val playerManager: AudioPlayerManager = DefaultAudioPlayerManager()

    init {
        AudioSourceManagers.registerRemoteSources(playerManager)
        AudioSourceManagers.registerLocalSource(playerManager)
    }

    @Synchronized private fun getGuildAudioPlayer(guild: IGuild): GuildMusicManager {
        val guildId = guild.longID
        var musicManager = musicManagers[guildId]

        if (musicManager == null) {
            musicManager = GuildMusicManager(playerManager)
            musicManagers.put(guildId, musicManager)
        }

        guild.audioManager.audioProvider = musicManager.getAudioProvider()

        return musicManager
    }

    fun loadUrl(channel: IChannel, trackUrl: String){
        val musicManager = getGuildAudioPlayer(channel.guild)

        playerManager.loadItemOrdered(musicManager, trackUrl, object : AudioLoadResultHandler {
            override fun trackLoaded(track: AudioTrack) {
                BotHelper.SendMessage(channel, "Adding to queue " + track.info.title)
                playTrack(channel.guild, track)
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                var firstTrack: AudioTrack? = playlist.selectedTrack
                if (firstTrack == null) {
                    firstTrack = playlist.tracks[0]
                }
                BotHelper.SendMessage(channel, "Adding to queue " + firstTrack!!.info.title + " (first track of playlist " + playlist.name + ")")
                playTrack(channel.guild, firstTrack)
            }

            override fun noMatches() {
                BotHelper.SendMessage(channel, "Nothing found by " + trackUrl)
            }

            override fun loadFailed(exception: FriendlyException) {
                BotHelper.SendMessage(channel, "Could not play: " + exception.message)
            }
        })
    }

    fun playTrack(guild: IGuild, track: AudioTrack){
        getGuildAudioPlayer(guild).scheduler.queue(track)
    }

    fun stopTrack(guild: IGuild){
        getGuildAudioPlayer(guild).player.stopTrack()
    }

    fun skipTrack(guild: IGuild) {
        val musicManager = getGuildAudioPlayer(guild).scheduler.nextTrack()
    }
}