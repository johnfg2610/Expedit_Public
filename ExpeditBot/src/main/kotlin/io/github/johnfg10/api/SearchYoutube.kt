package io.github.johnfg10.api

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.net.URLEncoder
import java.awt.SystemColor.text
import java.net.URLDecoder
import java.awt.SystemColor.text

class SearchYoutube(searchQuery: String) {
    val document: Document

    init {
        document = Jsoup.connect(googleUrl + URLEncoder.encode(searchQuery, "UTF-8")).userAgent(userAgent).get()

        //println(document.body())
    }

    fun getResults(resultAmount: Int = 0) : List<YoutubeResult> {
        var currentAmount = 0
        val resultList = mutableListOf<YoutubeResult>()

        val ytLookup = document.selectFirst("#results").select(".yt-lockup")

        ytLookup.forEach {
            if (currentAmount >= resultAmount && resultAmount != 0){
                return resultList
            }

            val thumbnailObj = it.selectFirst(".yt-lockup-thumbnail")
            val ytContent = it.selectFirst(".yt-lockup-content")

            val ytTitle = ytContent.selectFirst(".yt-lockup-title").text()
            val ytUrl = thumbnailObj.selectFirst("a").absUrl("href")
            val ytDescription = ytContent.select(".yt-lockup-description").text()
            val ytThumbnail = thumbnailObj.selectFirst("a").selectFirst("div").selectFirst("span").selectFirst("img").absUrl("src")

            resultList.add(YoutubeResult(ytTitle, ytUrl, ytDescription, ytThumbnail))

            currentAmount++
        }
        return resultList
    }

    companion object {
        val googleUrl = "https://www.youtube.com/results?search_query="
        val userAgent = "Expedit 1.0 (+http://example.com)"
    }
}

data class YoutubeResult(val title: String, val url: String, val description: String, val thumbnailUrl: String)