package io.github.johnfg10.utils.websiteHelpers;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.github.johnfg10.utils.mathsHelper.ClampRange;
import org.json.JSONObject;

import javax.xml.ws.http.HTTPException;

/**
 * Created by johnfg10 on 30/03/2017.
 */
public class YoutubeHelper {
    public static JSONObject youtubeSearchRaw(String keyword, String apiKey, int maxResults) throws UnirestException {
        maxResults = ClampRange.clampRange(maxResults, 1, 50);
        keyword = WebsiteHelpers.RemoveSpaces(keyword);
        String baseUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=%1$d&order=relevance&q=%2$s&type=video&videoDefinition=any&key=%3$s";
        String formattedUrl = String.format(baseUrl, maxResults, keyword, apiKey);
        HttpResponse<JsonNode> jsonNodeHttpResponse = Unirest.get(formattedUrl).asJson();
        System.out.println(formattedUrl);
        if (jsonNodeHttpResponse.getStatus() != 200){
            System.out.println(jsonNodeHttpResponse.getStatus() + jsonNodeHttpResponse.getStatusText());
            throw new HTTPException(jsonNodeHttpResponse.getStatus());
        }
        return jsonNodeHttpResponse.getBody().getObject();
    }

    public static JSONObject getFirstItem(JSONObject rawSearch){
        return (JSONObject)rawSearch.getJSONArray("items").get(0);
    }
}
