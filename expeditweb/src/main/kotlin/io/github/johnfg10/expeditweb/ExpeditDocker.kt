package io.github.johnfg10.expeditweb

import com.spotify.docker.client.DockerClient
import com.spotify.docker.client.messages.ContainerConfig
import com.spotify.docker.client.messages.ContainerCreation

class ExpeditDocker(){
    companion object {
        fun SetupExpeditDocker(dockerClient: DockerClient) : String {
            dockerClient.pull("johnfg10/expedit")

            val containerConfig = ContainerConfig
                    .builder()
                    .image("johnfg10/expedit")
                    .exposedPorts("80", "8080")
                    .cmd("java -jar expedit.jar")
                    .build()

            val container = dockerClient.createContainer(containerConfig)

            val id = container.id() ?: throw IllegalStateException("Container ID was null")

            dockerClient.startContainer(container.id())
            return id
        }
    }
}