package io.github.johnfg10.modules.music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import org.jetbrains.exposed.sql.transactions.TransactionManager.Companion.manager



class GuildMusicManager(val audioPlayerManager: AudioPlayerManager) {
    lateinit var scheduler: TrackScheduler
    lateinit var audioPlayer: AudioPlayer

    init {
        audioPlayer = audioPlayerManager.createPlayer();
        scheduler = TrackScheduler(audioPlayer)
        audioPlayer.addListener(scheduler);
    }


    fun getAudioProvider(): AudioProvider {
        return AudioProvider(audioPlayer)
    }
}