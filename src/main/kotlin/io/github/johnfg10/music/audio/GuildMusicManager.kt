package io.github.johnfg10.music.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager



class GuildMusicManager (val manager: AudioPlayerManager) {


    /**
     * Audio player for the guild.
     */
    val player: AudioPlayer = manager.createPlayer()
    /**
     * Track scheduler for the player.
     */
    val scheduler: TrackScheduler = TrackScheduler(player)

    init {
        player.addListener(scheduler)
    }

    /**
     * @return Wrapper around AudioPlayer to use it as an AudioSendHandler.
     */
    fun getAudioProvider(): AudioProvider {
        return AudioProvider(player)
    }
}