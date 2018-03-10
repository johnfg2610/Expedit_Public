package io.github.johnfg10

import io.github.johnfg10.rabbitmq.RabbitMQConfig

data class ExpeditConfig(val discordApiToken: String, val bitlyToken: String, val rabbitMQ: RabbitMQConfig){
    companion object {
        fun getDefault() : ExpeditConfig{
            return ExpeditConfig("", "", RabbitMQConfig.Default())
        }
    }
}