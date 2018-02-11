package io.github.johnfg10.music.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import sx.blah.discord.handle.audio.IAudioProcessor
import sx.blah.discord.handle.audio.IAudioProvider
import sx.blah.discord.handle.audio.AudioEncodingType
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame



class AudioProvider (var player: AudioPlayer) : IAudioProcessor {
    override fun setProvider(provider: IAudioProvider?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var lastFrame: AudioFrame? = null

    /**
     * @param audioPlayer Audio player to wrap.
     */
    fun AudioProvider(audioPlayer: AudioPlayer) {
        this.player = audioPlayer
    }

    override fun isReady(): Boolean {
        if (lastFrame == null) {
            lastFrame = player.provide()
        }

        return lastFrame != null
    }

    override fun provide(): ByteArray? {
        if (lastFrame == null) {
            lastFrame = player.provide()
        }

        val data = if (lastFrame != null) lastFrame!!.data else null
        lastFrame = null

        return data
    }

    override fun getChannels(): Int {
        return 2
    }

    override fun getAudioEncodingType(): AudioEncodingType {
        return AudioEncodingType.OPUS
    }
}