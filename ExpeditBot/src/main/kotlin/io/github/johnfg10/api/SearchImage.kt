package io.github.johnfg10.api

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.net.URLEncoder
import java.awt.SystemColor.text
import java.net.URLDecoder
import java.awt.SystemColor.text

class SearchImage(searchQuery: String) {
    val document: Document

    init {
        document = Jsoup.connect(googleUrl + URLEncoder.encode(searchQuery, "UTF-8")).userAgent(userAgent).get()
        //println(document.body())
    }

    fun getResults(resultAmount: Int = 0) : List<GoogleImageResult> {
        var currentAmount = 0
        val resultList = mutableListOf<GoogleImageResult>()

        val imageTableBody = document.select(".images_table>tbody")
        val tableRows = imageTableBody.select("tr")
        val tableDetails = tableRows.select("td")

        tableDetails.forEach {
            if (currentAmount >= resultAmount && resultAmount != 0){
                return resultList
            }

            val imageUrl = it.selectFirst("img").absUrl("src")

            val cite = it.selectFirst("cite").text()

            val description = it.text()

            resultList.add(GoogleImageResult(imageUrl, cite, description))
            currentAmount++
        }
        return resultList
    }

    companion object {
        val googleUrl = "https://www.google.co.uk/search?tbm=isch&q="
        val userAgent = "Expedit 1.0 (+http://example.com)"
    }
}

data class GoogleImageResult(val imageUrl: String, val cite: String, val description: String)