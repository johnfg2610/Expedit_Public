package io.github.johnfg10.config

import io.github.johnfg10.ExpeditConfig
import io.github.johnfg10.rabbitmq.RabbitMQConfig

class EnviormentConfigProvider : IConfigProvider {
    override fun getConfig(): ExpeditConfig {
        return ExpeditConfig(System.getenv("DISCORD_TOKEN"), System.getenv("BITLY_TOKEN"),
                RabbitMQConfig(System.getenv("RABBITMQ_USERNAME"), System.getenv("RABBITMQ_PASSWORD"),
                        System.getenv("RABBITMQ_VIRTUALHOST"), System.getenv("RABBITMQ_HOSTNAME"),
                        System.getenv("RABBITMQ_PORT").toInt()) )
    }

}