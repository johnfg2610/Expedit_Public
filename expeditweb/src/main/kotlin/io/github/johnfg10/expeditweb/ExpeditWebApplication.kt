package io.github.johnfg10.expeditweb

import com.spotify.docker.client.DefaultDockerClient
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class ExpeditWebApplication{

    companion object {
        val docker = DefaultDockerClient.fromEnv().build()
        init {
            println("Docker is: ${if (docker == null){"null"}else{"not null"}}")
        }
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(ExpeditWebApplication::class.java, *args)
}
