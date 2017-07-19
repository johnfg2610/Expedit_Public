package io.github.johnfg10.commands;

import com.google.common.util.concurrent.SettableFuture;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.TrackSearchRequest;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.Track;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import io.github.johnfg10.ExpeditConst;
import io.github.johnfg10.utils.AudioHelper;
import io.github.johnfg10.utils.GuildMusicManager;
import io.github.johnfg10.utils.RequestBufferHelper;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import io.github.johnfg10.utils.AudioUtils;
import sx.blah.discord.util.audio.AudioPlayer;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class SpotifyCommandHandlers implements CommandExecutor {
    @Command(aliases = {"sptrack"}, description = "Lists the playlist")
    public String onCommandSptrack(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException, UnirestException {
        String cleanMsg = message.getContent().replaceAll(command, "");
        try {
            TrackSearchRequest trackSearchRequest = ExpeditConst.SPOTIFYAPI.searchTracks(cleanMsg).limit(1).build();
            SettableFuture<Page<Track>> pageSettableFuture =  trackSearchRequest.getAsync();
            Page<Track> trackPage = new Page<>();
            if (pageSettableFuture.set(trackPage)){
                AudioPlayer audioPlayer = AudioUtils.getAudioPlayer(guild);
                String uri = trackPage.getItems().get(0).getUri();
                System.out.println(uri);
                audioPlayer.queue(new URL(uri));
                return "Song added to queue";
            }else{
                return "Song was not added";
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        return "Song was not added";
    }

    @Command(aliases = {"spartist"}, description = "Lists the playlist")
    public void onCommandSpartist(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException, UnirestException {

    }

    @Command(aliases = {"spalbum"}, description = "Lists the playlist")
    public void onCommandSpalbum(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException, UnirestException {

    }
}
