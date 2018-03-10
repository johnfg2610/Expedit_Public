package io.github.johnfg10.config

import com.cars.framework.secrets.DockerSecrets
import io.github.johnfg10.ExpeditConfig
import io.github.johnfg10.rabbitmq.RabbitMQConfig

class DockerConfigProvider : IConfigProvider {
    val dockerSecrets :Map<String, String> = DockerSecrets.load()

    override fun getConfig(): ExpeditConfig {
        return ExpeditConfig(dockerSecrets["discordApiToken"] ?: "", dockerSecrets["bitlyToken"] ?: "", RabbitMQConfig.Default())
    }
}