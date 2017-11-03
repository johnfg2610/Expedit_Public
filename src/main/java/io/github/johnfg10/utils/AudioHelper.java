package io.github.johnfg10.utils;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import sx.blah.discord.Discord4J;
import sx.blah.discord.handle.audio.IAudioManager;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.MissingPermissionsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Created by johnfg10 on 28/03/2017.
 */
public class AudioHelper {

    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    public AudioHelper(){
        this.musicManagers = new HashMap<>();

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    public synchronized GuildMusicManager getGuildAudioPlayer(IGuild guild) {
        long guildId = Long.parseLong(guild.getStringID());
        GuildMusicManager musicManager = this.musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            this.musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setAudioProvider(musicManager.getAudioProvider());

        return musicManager;
    }

    public void loadAndPlay(final IChannel channel, final String trackUrl) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.player.setPaused(false);

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                sendMessageToChannel(channel, "Adding to queue " + track.getInfo().title);
                play(channel.getGuild(), musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }

                sendMessageToChannel(channel, "Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")");

                play(channel.getGuild(), musicManager, firstTrack);
            }

            @Override
            public void noMatches() {
                sendMessageToChannel(channel, "Nothing found by " + trackUrl);
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                sendMessageToChannel(channel, "Could not play: " + exception.getMessage());
            }
        });
    }


    public List<String> loadAndPlay(final IChannel channel, final String trackUrl, List<String> strings) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.player.setPaused(false);

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                strings.add("Adding to queue " + track.getInfo().title);
                play(channel.getGuild(), musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }

                strings.add("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")");

                play(channel.getGuild(), musicManager, firstTrack);
            }

            @Override
            public void noMatches() {
                strings.add("Nothing found by " + trackUrl);
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                strings.add("Could not play: " + exception.getMessage());
            }
        });

        return strings;
    }

    public void loadAndPlayQuietly(final IChannel channel, final String trackUrl) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.player.setPaused(false);
        //String string = "";

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                //string = "Adding to queue " + track.getInfo().title;
                play(channel.getGuild(), musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }

                //sendMessageToChannel(channel, "Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")");

                play(channel.getGuild(), musicManager, firstTrack);
            }

            @Override
            public void noMatches() {
                //sendMessageToChannel(channel, "Nothing found by " + trackUrl);
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                sendMessageToChannel(channel, "Could not play: " + exception.getMessage());
            }
        });

    }

    private void play(IGuild guild, GuildMusicManager musicManager, AudioTrack track) {
        connectToFirstVoiceChannel(guild.getAudioManager());

        musicManager.scheduler.queue(track);
    }

    public void skipTrack(IChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.scheduler.nextTrack();

        sendMessageToChannel(channel, "Skipped to next track.");
    }

    private void sendMessageToChannel(IChannel channel, String message) {
        RequestBufferHelper.RequestBuffer(channel, message);
    }

    public static void connectToFirstVoiceChannel(IAudioManager audioManager) {
        for (IVoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
            if (voiceChannel.isConnected()) {
                return;
            }
        }

        for (IVoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
            try {
                voiceChannel.join();
            } catch (MissingPermissionsException e) {
                Discord4J.LOGGER.warn("Cannot enter voice channel {}", voiceChannel.getName(), e);
            }
        }
    }

    public void setVolume(IGuild guild, int volume){
        if (volume >= 1001){
            volume = 1000;
        }
        if (volume <= 0){
            volume = 1;
        }
        getGuildAudioPlayer(guild).player.setVolume(volume);
    }

    public void pause(IGuild guild){
        AudioPlayer audioPlayer = getGuildAudioPlayer(guild).player;
        if(audioPlayer.isPaused()){
            audioPlayer.setPaused(false);
        }else {
            audioPlayer.setPaused(true);
        }
    }

    public AudioTrack getInfo(IGuild guild){
        AudioPlayer audioPlayer = getGuildAudioPlayer(guild).player;
        return audioPlayer.getPlayingTrack();
    }

    public void stop(IGuild guild){
        getGuildAudioPlayer(guild).player.stopTrack();
    }

    public BlockingQueue<AudioTrack> getPlaylist(IGuild guild){
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        BlockingQueue<AudioTrack> musicQueue = musicManager.scheduler.getQueue();
        return musicQueue;
    }
}
