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
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by johnfg10 on 28/03/2017.
 */
public class AudioCommandHandler implements de.btobastian.sdcf4j.CommandExecutor {
    @Command(aliases = {"join", "joinme"})
    public void onCommandJoin(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException {
/*        if (user.getConnectedVoiceChannels() == null || user.getConnectedVoiceChannels().size() <= 0){
            RequestBufferHelper.RequestBuffer(channel, "You are not in a voice channel!");
        }else {
            user.getConnectedVoiceChannels().get(0).join();
        }*/
        if (message.getChannel() == guild.getChannelsByName("musicchannel").get(0)) {
            if (user.getConnectedVoiceChannels() == null || user.getConnectedVoiceChannels().size() <= 0) {
                RequestBufferHelper.RequestBuffer(channel, "You are not in a voice channel!");
            } else {
                ExpeditConst.audioHelper.getGuildAudioPlayer(guild).player.setPaused(false);
                guild.getVoiceChannelsByName("Music_Channel").get(0).join();
            }
        }
    }

    @Command(aliases = {"leave", "leaveme"})
    public void onCommandLeave(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException {
        if (message.getChannel() == guild.getChannelsByName("musicchannel").get(0)) {
            guild.getConnectedVoiceChannel().leave();
            ExpeditConst.audioHelper.getGuildAudioPlayer(guild).player.setPaused(true);
        }
    }

    @Command(aliases = {"addqueue", "queue", "play"})
    public void onCommandQueue(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException {
        if (message.getChannel() == guild.getChannelsByName("musicchannel").get(0)) {
            if (!guild.getConnectedVoiceChannel().isConnected()) {
                RequestBufferHelper.RequestBuffer(channel, "Please use the join command first!");
            } else {
                ExpeditConst.audioHelper.loadAndPlay(message.getChannel(), args[0]);
            }
        }
    }

    @Command(aliases = {"skip", "jump"})
    public void onCommandSkip(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException {
        if (message.getChannel() == guild.getChannelsByName("musicchannel").get(0)) {
            if (!guild.getConnectedVoiceChannel().isConnected()) {
                RequestBufferHelper.RequestBuffer(channel, "Please use the join command first!");
            } else {
                ExpeditConst.audioHelper.skipTrack(message.getChannel());
            }
        }
    }

    @Command(aliases = {"searchplay", "yt", "ytt"})
    public void onCommandSearchPlay(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException, IOException, UnirestException {
        if (message.getChannel() == guild.getChannelsByName("musicchannel").get(0)) {
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

    @Command(aliases = {"vol", "volume"})
    public void onCommandVolume(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException {
        if (message.getChannel() == guild.getChannelsByName("musicchannel").get(0)) {
            if (!guild.getConnectedVoiceChannel().isConnected()) {

                RequestBufferHelper.RequestBuffer(channel, "Please use the join command first!");
            } else {
                ExpeditConst.audioHelper.setVolume(guild, Integer.parseInt(args[0]));
            }
        }
    }

    @Command(aliases = {"pause", "wait"})
    public void onCommandPause(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException {
        if (message.getChannel() == guild.getChannelsByName("musicchannel").get(0)) {
            if (!guild.getConnectedVoiceChannel().isConnected()) {
                RequestBufferHelper.RequestBuffer(channel, "Please use the join command first!");
            } else {
                ExpeditConst.audioHelper.pause(guild);
            }
        }
    }

    @Command(aliases = {"info", "trackinfo"})
    public void onCommandInfo(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException {
        if (message.getChannel() == guild.getChannelsByName("musicchannel").get(0)) {
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
                RequestBufferHelper.RequestBuffer(channel, "", embedBuilder.ignoreNullEmptyFields().build(), false);
            }

        }
    }

    @Command(aliases = {"stop"})
    public void onCommandStop(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException {
        if (message.getChannel() == guild.getChannelsByName("musicchannel").get(0)) {
            if (!guild.getConnectedVoiceChannel().isConnected()) {
                RequestBufferHelper.RequestBuffer(channel, "Please use the join command first!");
            } else {
                ExpeditConst.audioHelper.stop(guild);
            }
        }
    }


    @Command(aliases = {"ytplaylist", "ytpl", "pl"})
    public void onCommandPlaylist(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException, UnirestException {
        if (message.getChannel() == guild.getChannelsByName("musicchannel").get(0)) {
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

    @Command(aliases = {"playlistinfo", "plinfo"})
    public void onCommandPlaylistInfo(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException, UnirestException {
        if (message.getChannel() == guild.getChannelsByName("musicchannel").get(0)) {
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
}
