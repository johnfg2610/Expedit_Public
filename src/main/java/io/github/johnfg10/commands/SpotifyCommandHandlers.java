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
import io.github.johnfg10.Expedit;
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

            final TrackSearchRequest request = ExpeditConst.SPOTIFYAPI.searchTracks(cleanMsg).build();
            final Page<Track> trackSearchResult = request.get();
            System.out.println("I got " + trackSearchResult.getTotal() + " results!");

            Track track = trackSearchResult.getItems().get(1);
            System.out.println(track.getUri());
            AudioUtils.getAudioPlayer(guild).queue(new URL(track.getUri()));

            return "Song added to queue";


        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (WebApiException e) {
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
