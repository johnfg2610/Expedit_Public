package io.github.johnfg10.nationalrail

import com.beust.klaxon.Klaxon
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.sun.javaws.exceptions.InvalidArgumentException
import io.github.johnfg10.nationalrail.authenticate.Authenticate
import io.github.johnfg10.nationalrail.incident.Incident
import io.github.johnfg10.nationalrail.incident.Incidents
import org.simpleframework.xml.core.Persister
import jdk.nashorn.internal.objects.NativeRegExp.source
import org.h2.util.JdbcUtils.serializer





open class NationalRailApi{

    val auth: Authenticate

    constructor(username: String, password: String){
        auth = Authenticate(username, password) ?: throw InvalidArgumentException(arrayOf("Authentication could not be established!"))
    }

    constructor(authenticate: Authenticate){
        auth = authenticate
    }

    fun Authenticate(username: String, password: String) : Authenticate?{
        var totalResult: Authenticate? = null
        "https://datafeeds.nationalrail.co.uk/authenticate".httpGet(listOf(Pair("username", username), Pair("password", password))).responseString{request, response, result ->
            run {
                totalResult = when (result) {
                    is Result.Success -> {
                        Klaxon().parse<Authenticate>(result.value)
                    }
                    is Result.Failure -> {
                        null
                    }
                }
            }
        }

        return totalResult
    }

    fun GetIncidents(): Incidents? {
        var incident: Incidents? = null
        "https://datafeeds.nationalrail.co.uk/api/staticfeeds/5.0/incidents ".httpGet(listOf(Pair("X-Auth-Token", auth.token))).responseString { request, response, result ->
            run {
                incident = when (result) {
                    is Result.Success -> {
                        val serializer = Persister()
                        serializer.read(Incidents::class.java, result.value)
                    }
                    is Result.Failure -> {
                        null
                    }
                }
            }
        }

        return incident
    }

}