package io.github.johnfg10.api

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.net.URLEncoder
import java.awt.SystemColor.text
import java.net.URLDecoder
import java.awt.SystemColor.text

class Search(searchQuery: String) {
    val document: Document

    init {
        document = Jsoup.connect(googleUrl + URLEncoder.encode(searchQuery, "UTF-8")).userAgent(userAgent).get()
    }

    fun getResults(resultAmount: Int = 0) : List<GoogleResult> {
        var currentAmount = 0
        val resultList = mutableListOf<GoogleResult>()



        for (search: Element in document.select(".g")){
            if (currentAmount >= resultAmount && resultAmount != 0){
                return resultList
            }

            val contentSelect = search.selectFirst(".st")
            if (contentSelect == null){
                continue
            }
            val content = contentSelect.text()

            val linkSelect = search.selectFirst(".r")
            if (linkSelect == null){
                resultList.add(GoogleResult("Not found", "Not found", content))
                currentAmount++
                continue
            }
            val elements = linkSelect.getElementsByTag("a")
            if (elements == null){
                resultList.add(GoogleResult("Not found", "Not found", content))
                currentAmount++
                continue
            }
            val element = elements[0]


/*            if (element == null){
                println("element emptys")
                return emptyList()
            }*/

            val title = element.text()
            var url = element.absUrl("href")


            url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8")
            if (!url.startsWith("http") || !url.startsWith("https")) {
                continue // Ads/news/etc.
            }

            resultList += GoogleResult(title, url, content)

            currentAmount++
        }

        return resultList
    }

    companion object {
        val googleUrl = "https://www.google.co.uk/search?client=ubuntu&q="
        val userAgent = "Expedit 1.0 (+http://example.com)"
    }
}

data class GoogleResult(val title: String, val url: String, val description: String)