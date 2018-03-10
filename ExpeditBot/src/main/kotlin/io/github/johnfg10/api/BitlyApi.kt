package io.github.johnfg10.api

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import io.github.johnfg10.Expedit
import io.github.johnfg10.exception
import org.slf4j.LoggerFactory

class BitlyApi {
    companion object {
        val logger = LoggerFactory.getLogger(this::class.java)
    }


    val API_ADDRESS = "https://api-ssl.bitly.com"
    val ACCESS_TOKEN = Expedit.expeditConfig.bitlyToken

    fun shortenUrl(longUrl: String) : BitlyShortenResult? {
        if (ACCESS_TOKEN.isEmpty())
            return null
        var bitlyResult: BitlyShortenResult? = null

        "$API_ADDRESS/v3/shorten".httpGet(listOf(Pair("access_token", ACCESS_TOKEN), Pair("longUrl", longUrl)))
                .responseObject {
                    request: Request, response: Response, result: Result<BitlyShortenResult, FuelError> ->
                    when(result){
                        is Result.Success -> {
                            bitlyResult = result.value
                        }
                        is Result.Failure -> {
                            val err = result.error
                            logger.exception(err)
                        }
                    }
                }
        return bitlyResult
    }
}

/**
 * {"status_code":200,"status_txt":"OK","data":{"url":"http://bit.ly/2FYWTBU","hash":"2FYWTBU","global_hash":"9Hy8WL","long_url":"http://www.test.com/","new_hash":0}}
 */

class BitlyShortenResult(var statusCode: Int, var statusText: String, var data: BitlyShortenData)

class BitlyShortenData(var url: String, var hash: String, var globalHash: String, var longUrl: String, var newHash: Int)

