package io.github.johnfg10.modules.music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import java.util.concurrent.BlockingQueue
import sun.audio.AudioPlayer.player
import sun.audio.AudioPlayer.player
import java.util.concurrent.LinkedBlockingQueue


class TrackScheduler(val player: AudioPlayer, val queue: BlockingQueue<AudioTrack> = LinkedBlockingQueue()) : AudioEventAdapter() {

    fun queue(audioTrack: AudioTrack){
        if(!player.startTrack(audioTrack, true)){
            queue.offer(audioTrack)
        }
    }

    fun nextTrack() {
        player.startTrack(queue.poll(), false)
    }

    override fun onPlayerPause(player: AudioPlayer) {
        super.onPlayerPause(player)
    }

    override fun onPlayerResume(player: AudioPlayer) {
        super.onPlayerResume(player)
    }

    override fun onTrackStart(player: AudioPlayer, track: AudioTrack?) {
        super.onTrackStart(player, track)
    }

    override fun onTrackEnd(player: AudioPlayer, track: AudioTrack, endReason: AudioTrackEndReason) {
        super.onTrackEnd(player, track, endReason)
    }
}