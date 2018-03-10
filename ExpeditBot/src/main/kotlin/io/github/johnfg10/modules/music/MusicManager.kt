package io.github.johnfg10.modules.music

import sx.blah.discord.util.MissingPermissionsException
import sx.blah.discord.handle.obj.IVoiceChannel
import sx.blah.discord.handle.audio.IAudioManager
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IGuild
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.api.events.EventSubscriber
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import sx.blah.discord.api.ClientBuilder
import sx.blah.discord.api.IDiscordClient
import org.slf4j.LoggerFactory

class MusicManager(val musicManagers: MutableMap<Long, GuildMusicManager> = mutableMapOf()){
    private val playerManager: AudioPlayerManager = DefaultAudioPlayerManager()
    init {
        AudioSourceManagers.registerRemoteSources(playerManager)
        AudioSourceManagers.registerLocalSource(playerManager)
    }

    @Synchronized
    fun getGuildAudioPlayer(guild: IGuild): GuildMusicManager {
        val guildId = guild.longID
        var musicManager: GuildMusicManager? = musicManagers.get(guildId)

        if (musicManager == null) {
            musicManager = GuildMusicManager(playerManager)
            musicManagers[guildId] = musicManager
        }

        guild.audioManager.audioProvider = musicManager.getAudioProvider()

        return musicManager
    }


    fun loadAndPlay(channel: IChannel, trackUrl: String) {
        val musicManager = getGuildAudioPlayer(channel.guild)

        playerManager.loadItemOrdered(musicManager, trackUrl, object : AudioLoadResultHandler {
            override fun trackLoaded(track: AudioTrack) {
                sendMessageToChannel(channel, "Adding to queue " + track.info.title)

                play(channel.guild, musicManager, track)
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                var firstTrack: AudioTrack? = playlist.selectedTrack

                if (firstTrack == null) {
                    firstTrack = playlist.tracks[0]
                }

                sendMessageToChannel(channel, "Adding to queue " + firstTrack!!.info.title + " (first track of playlist " + playlist.name + ")")

                play(channel.guild, musicManager, firstTrack)
            }

            override fun noMatches() {
                sendMessageToChannel(channel, "Nothing found by $trackUrl")
            }

            override fun loadFailed(exception: FriendlyException) {
                sendMessageToChannel(channel, "Could not play: " + exception.message)
            }
        })
    }

    private fun play(guild: IGuild, musicManager: GuildMusicManager, track: AudioTrack?) {
        connectToFirstVoiceChannel(guild.audioManager)

        musicManager.scheduler.queue(track!!)
    }

    private fun skipTrack(channel: IChannel) {
        val musicManager = getGuildAudioPlayer(channel.guild)
        musicManager.scheduler.nextTrack()

        sendMessageToChannel(channel, "Skipped to next track.")
    }

    private fun sendMessageToChannel(channel: IChannel, message: String) {
        try {
            channel.sendMessage(message)
        } catch (e: Exception) {
            log.warn("Failed to send message {} to {}", message, channel.name, e)
        }
    }

    private fun connectToFirstVoiceChannel(audioManager: IAudioManager) {
        for (voiceChannel in audioManager.guild.voiceChannels) {
            if (voiceChannel.isConnected) {
                return
            }
        }

        for (voiceChannel in audioManager.guild.voiceChannels) {
            try {
                voiceChannel.join()
            } catch (e: MissingPermissionsException) {
                log.warn("Cannot enter voice channel {}", voiceChannel.name, e)
            }

        }
    }

    companion object {
        val log = LoggerFactory.getLogger(MusicManager::class.java)
    }
}