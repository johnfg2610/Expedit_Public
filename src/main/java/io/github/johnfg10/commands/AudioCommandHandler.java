package io.github.johnfg10.commands;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import de.btobastian.sdcf4j.Command;
import io.github.johnfg10.Expedit;
import io.github.johnfg10.ExpeditConst;
import io.github.johnfg10.utils.AudioHelper;
import io.github.johnfg10.utils.AudioUtils;
import io.github.johnfg10.utils.GuildMusicManager;
import io.github.johnfg10.utils.RequestBufferHelper;
import io.github.johnfg10.utils.storage.GeneralSettingsDatabaseUtils;
import io.github.johnfg10.utils.websiteHelpers.YoutubeHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by johnfg10 on 28/03/2017.
 */
public class AudioCommandHandler implements de.btobastian.sdcf4j.CommandExecutor {
    @Command(aliases = {"join", "joinme"}, description = "Moves Expedit bot to your channel for music!")
    public void onCommandJoin(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException {
        System.out.println("will do");

        musicChannels mc = this.musicHelper(channel, guild);

        String musicText = mc.getMusicText();
        String musicVoice = mc.getMusicVoice();
        System.out.println(musicText);
        if (message.getChannel() == guild.getChannelsByName(musicText).get(0) || !mc.isShouldUseText()){
            if (user.getVoiceStatesLong() == null || user.getVoiceStatesLong().size() <= 0) {
                RequestBufferHelper.RequestBuffer(channel, "You are not in a voice channel!");
            } else {
                ExpeditConst.audioHelper.getGuildAudioPlayer(guild).player.setPaused(false);
                guild.getVoiceChannelsByName(musicVoice).get(0).join();
            }
        }
    }

    @Command(aliases = {"leave", "leaveme"}, description = "Tells Expedit bot to leave you alone")
    public void onCommandLeave(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException {
        musicChannels mc = this.musicHelper(channel, guild);

        String musicText = mc.getMusicText();
        String musicVoice = mc.getMusicVoice();

        if (message.getChannel() == guild.getChannelsByName(musicText).get(0) || !mc.isShouldUseText()) {
            guild.getConnectedVoiceChannel().leave();
            ExpeditConst.audioHelper.getGuildAudioPlayer(guild).player.setPaused(true);
        }
    }

    @Command(aliases = {"addqueue", "queue", "play"}, description = "Adds a song to the playlist")
    public void onCommandQueue(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException {
        musicChannels mc = this.musicHelper(channel, guild);

        String musicText = mc.getMusicText();
        String musicVoice = mc.getMusicVoice();

        if (message.getChannel() == guild.getChannelsByName(musicText).get(0) || !mc.isShouldUseText()) {
            if (!guild.getConnectedVoiceChannel().isConnected()) {
                RequestBufferHelper.RequestBuffer(channel, "Please use the join command first!");
            } else {
                ExpeditConst.audioHelper.loadAndPlay(message.getChannel(), args[0]);
            }
        }
    }

    @Command(aliases = {"skip", "jump"}, description = "Skips the song")
    public void onCommandSkip(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException {
        musicChannels mc = this.musicHelper(channel, guild);

        String musicText = mc.getMusicText();
        String musicVoice = mc.getMusicVoice();

        if (message.getChannel() == guild.getChannelsByName(musicText).get(0) || !mc.isShouldUseText()) {
            if (!guild.getConnectedVoiceChannel().isConnected()) {
                RequestBufferHelper.RequestBuffer(channel, "Please use the join command first!");
            } else {
                ExpeditConst.audioHelper.skipTrack(message.getChannel());
            }
        }
    }

    @Command(aliases = {"searchplay", "yt", "ytt"}, description = "Look for a song on Youtube")
    public void onCommandSearchPlay(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException, IOException, UnirestException {
        musicChannels mc = this.musicHelper(channel, guild);

        String musicText = mc.getMusicText();
        String musicVoice = mc.getMusicVoice();

        if (message.getChannel() == guild.getChannelsByName(musicText).get(0) || !mc.isShouldUseText()) {
            if (!guild.getConnectedVoiceChannel().isConnected()) {
                RequestBufferHelper.RequestBuffer(channel, "Please use the join command first!");
            } else {
                String baseUrl = "https://www.youtube.com/watch?v=%1s";
                String searchTerm = message.getContent();
                System.out.println(command);
                searchTerm = searchTerm.replace(command, "");

                JSONObject youtubeRaw = YoutubeHelper.youtubeSearchRaw(searchTerm, ExpeditConst.youtubeApi, 1);
                JSONObject firstObject = YoutubeHelper.getFirstItem(youtubeRaw);
                JSONObject snippet = firstObject.getJSONObject("snippet");

                String title = snippet.getString("title");
                String description = snippet.getString("description");
                String thumbnailUrl = snippet.getJSONObject("thumbnails").getJSONObject("high").getString("url");
                String videoId = firstObject.getJSONObject("id").getString("videoId");
                String formmatedUrl = String.format(baseUrl, videoId);

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.withTitle(title);
                embedBuilder.withDescription(description);
                embedBuilder.withImage(thumbnailUrl);
                RequestBufferHelper.RequestBuffer(channel, "", embedBuilder, false);
                ExpeditConst.audioHelper.loadAndPlayQuietly(channel, formmatedUrl);
            }
        }
    }

    @Command(aliases = {"volume", "vol"}, description = "Adjusts the volume" )
    public void onCommandVolume(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException {
        musicChannels mc = this.musicHelper(channel, guild);

        String musicText = mc.getMusicText();
        String musicVoice = mc.getMusicVoice();

        if (message.getChannel() == guild.getChannelsByName(musicText).get(0) || !mc.isShouldUseText()) {
            if (!guild.getConnectedVoiceChannel().isConnected()) {

                RequestBufferHelper.RequestBuffer(channel, "Please use the join command first!");
            } else {
                ExpeditConst.audioHelper.setVolume(guild, Integer.parseInt(args[0]));
            }
        }
    }

    @Command(aliases = {"pause", "wait"}, description = "Stops the song")
    public void onCommandPause(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException {
        musicChannels mc = this.musicHelper(channel, guild);

        String musicText = mc.getMusicText();
        String musicVoice = mc.getMusicVoice();

        if (message.getChannel() == guild.getChannelsByName(musicText).get(0) || !mc.isShouldUseText()) {
            if (!guild.getConnectedVoiceChannel().isConnected()) {
                RequestBufferHelper.RequestBuffer(channel, "Please use the join command first!");
            } else {
                ExpeditConst.audioHelper.pause(guild);
            }
        }
    }

    @Command(aliases = {"info", "trackinfo"}, description = "List whats currently playing")
    public void onCommandInfo(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException {
        musicChannels mc = this.musicHelper(channel, guild);

        String musicText = mc.getMusicText();
        String musicVoice = mc.getMusicVoice();

        if (message.getChannel() == guild.getChannelsByName(musicText).get(0) || !mc.isShouldUseText()) {
            if (!guild.getConnectedVoiceChannel().isConnected()) {
                RequestBufferHelper.RequestBuffer(channel, "Please use the join command first!");
            } else {
                AudioTrack audioTrackInfo = ExpeditConst.audioHelper.getInfo(guild);
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.appendField("Title", audioTrackInfo.getInfo().title, false);
                embedBuilder.appendField("Author", audioTrackInfo.getInfo().author, false);
                embedBuilder.appendField("Duration", String.valueOf(audioTrackInfo.getDuration()), false);
                embedBuilder.appendField("Current Position", String.valueOf(audioTrackInfo.getPosition()), false);
                embedBuilder.appendField("Source name", audioTrackInfo.getSourceManager().getSourceName(), false);
                RequestBufferHelper.RequestBuffer(channel, "", embedBuilder.setLenient(false).build(), false);
            }

        }
    }

    @Command(aliases = {"stop"}, description = "Ends a song")
    public void onCommandStop(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException {
        musicChannels mc = this.musicHelper(channel, guild);

        String musicText = mc.getMusicText();
        String musicVoice = mc.getMusicVoice();

        if (message.getChannel() == guild.getChannelsByName(musicText).get(0) || !mc.isShouldUseText()) {
            if (!guild.getConnectedVoiceChannel().isConnected()) {
                RequestBufferHelper.RequestBuffer(channel, "Please use the join command first!");
            } else {
                ExpeditConst.audioHelper.stop(guild);
            }
        }
    }


    @Command(aliases = {"ytplaylist", "ytpl", "pl"}, description = "Sets the bots playlist")
    public void onCommandPlaylist(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException, UnirestException {
        musicChannels mc = this.musicHelper(channel, guild);

        String musicText = mc.getMusicText();
        String musicVoice = mc.getMusicVoice();

        if (message.getChannel() == guild.getChannelsByName(musicText).get(0) || !mc.isShouldUseText()) {
            if (!guild.getConnectedVoiceChannel().isConnected()) {
                RequestBufferHelper.RequestBuffer(channel, "Please use the join command first!");
            } else {
                //String msg = message.getContent().replaceFirst(command, "");
                Pattern pattern = Pattern.compile("&list=\\S*$");
                Matcher m = pattern.matcher(message.getContent());

                if (m.find()) {
                    String videoId = m.group().replaceFirst("&list=", "");
                    JSONArray jsonArray = null;

                    try {
                        jsonArray = AudioUtils.getPlaylistItems(videoId);
                    } catch (UnirestException e) {
                        e.printStackTrace();
                    }

                    for (Object jsonObj : jsonArray) {
                        JSONObject jsonObject = (JSONObject) jsonObj;
                        ExpeditConst.audioHelper.loadAndPlay(channel, "https://www.youtube.com/watch?v=" + jsonObject.getJSONObject("snippet").getJSONObject("resourceId").getString("videoId"));
                    }

                    RequestBufferHelper.RequestBuffer(channel, "Added the playlist to the bots playlist!");
                }
            }
        }
    }

    @Command(aliases = {"playlistinfo", "plinfo"}, description = "Lists the playlist")
    public void onCommandPlaylistInfo(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException, UnirestException {
        musicChannels mc = this.musicHelper(channel, guild);

        String musicText = mc.getMusicText();
        String musicVoice = mc.getMusicVoice();

        if (message.getChannel() == guild.getChannelsByName(musicText).get(0) || !mc.isShouldUseText()) {
            if (!guild.getConnectedVoiceChannel().isConnected()) {
                RequestBufferHelper.RequestBuffer(channel, "Please use the join command first!");
            } else {
                AudioHelper audioHelper = ExpeditConst.audioHelper;
                GuildMusicManager guildMusicManager = audioHelper.getGuildAudioPlayer(guild);
                BlockingQueue<AudioTrack> audioTracks = ExpeditConst.audioHelper.getPlaylist(guild);

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.withTitle("Audio playlist");
                embedBuilder.appendField(guildMusicManager.player.getPlayingTrack().getInfo().title, guildMusicManager.player.getPlayingTrack().getDuration() + "ms", false);

                for (AudioTrack at:audioTracks) {
                    embedBuilder.appendField(at.getInfo().title, at.getDuration() + "ms", false);
                }

                RequestBufferHelper.RequestBuffer(channel, "", embedBuilder, false);
            }
        }
    }

    public musicChannels musicHelper(IChannel channel, IGuild guild) throws RateLimitException, DiscordException, MissingPermissionsException {
        String musicVoice = null;
        String musicText = null;
        boolean shouldUseText = true;
        try {
            musicVoice = ExpeditConst.databaseUtils.getSetting("musicVoice", guild.getStringID());
            musicText = ExpeditConst.databaseUtils.getSetting("musicText", guild.getStringID());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException e) {
            e.printStackTrace();
        }

        if (musicText == null){
            shouldUseText = false;
        }

        if (musicVoice == null){
            Random rnd = new Random();
            if (rnd.nextInt() < 1000){
                channel.sendMessage("It would appear there is no custom music voice channel setup reverting to default \n default: 'music_channel'");
            }
        }

        return new musicChannels(musicText, musicVoice, shouldUseText);
    }

    class musicChannels{
        private String musicText;
        private String musicVoice;
        private boolean shouldUseText;

        public musicChannels(String musicT, String musicV, boolean sut) {
            musicText = musicT;
            musicVoice = musicV;
            shouldUseText = sut;
        }

        public String getMusicText() {
            return musicText;
        }

        public String getMusicVoice() {
            return musicVoice;
        }

        public boolean isShouldUseText() {
            return shouldUseText;
        }
    }
}
