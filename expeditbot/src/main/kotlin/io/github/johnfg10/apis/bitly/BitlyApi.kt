package io.github.johnfg10.apis.bitly

import com.beust.klaxon.Json
import com.beust.klaxon.Klaxon
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import io.github.johnfg10.Expedit
import io.github.johnfg10.TokenConfig
import java.util.*
import javax.swing.text.html.Option

class BitlyApi {
    val API_ADDRESS = "https://api-ssl.bitly.com"
    val ACCESS_TOKEN = Expedit.configuration[TokenConfig.bitlyToken]

    fun shortenUrl(longUrl: String) : BitlyShortenResult? {
        var bitlyResult: BitlyShortenResult? = null

        val requestRef = "$API_ADDRESS/v3/shorten".httpGet(listOf(Pair("access_token",ACCESS_TOKEN), Pair("longUrl", longUrl))).responseString()
        val result = requestRef.third

        when (result) {
            is Result.Failure -> {
                val error = result.error
                Expedit.logger.error("${error.response} \n ${error.exception} \n ${error.stackTrace}")
            }
            is Result.Success -> {
                println(result.value)
                val klaxonRes = Klaxon().parse<BitlyShortenResult>(result.value)
                bitlyResult = klaxonRes
            }
        }

        return bitlyResult
    }
}

/**
 * {"status_code":200,"status_txt":"OK","data":{"url":"http://bit.ly/2FYWTBU","hash":"2FYWTBU","global_hash":"9Hy8WL","long_url":"http://www.test.com/","new_hash":0}}
 */

class BitlyShortenResult(@Json(name = "status_code") var statusCode: Int, @Json(name = "status_txt") var statusText: String, @Json(name = "data") var data: BitlyShortenData)

class BitlyShortenData(@Json(name = "url") var url: String, @Json(name = "hash") var hash: String, @Json(name = "global_hash") var globalHash: String, @Json(name = "long_url") var longUrl: String, @Json(name = "new_hash") var newHash: Int)

