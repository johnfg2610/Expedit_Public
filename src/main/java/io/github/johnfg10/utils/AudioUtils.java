package io.github.johnfg10.utils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.github.johnfg10.Expedit;
import io.github.johnfg10.ExpeditConst;
import org.json.JSONArray;
import org.json.JSONObject;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.audio.AudioPlayer;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.xml.ws.http.HTTPException;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by johnfg10 on 28/03/2017.
 */
public class AudioUtils {

    public static AudioPlayer getAudioPlayer(IGuild guild){
        return AudioPlayer.getAudioPlayerForGuild(guild);
    }

    public static JSONArray getPlaylistItems(String playlistID) throws UnirestException {
        String baseUrl = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId=%1s&key=%2s";

        HttpResponse<JsonNode> jsonNode = Unirest.get(String.format(baseUrl, playlistID, ExpeditConst.youtubeApi)).asJson();

        if (jsonNode.getStatus() == 401){
            throw new HTTPException(401);
        }

        JSONArray jsonArray = jsonNode.getBody().getObject().getJSONArray("items");

        return jsonArray;
    }




}
