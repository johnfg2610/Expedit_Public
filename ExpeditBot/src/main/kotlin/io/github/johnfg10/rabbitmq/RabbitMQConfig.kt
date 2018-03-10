package io.github.johnfg10.rabbitmq

data class RabbitMQConfig(val username: String, val password: String, val virtualHost: String, val hostname: String, val port: Int, val exchange: String = "expeditExchange", val queue: String = "expeditQueue", val routingKey: String = "expedit"){
    companion object {
        fun Default() : RabbitMQConfig {
            return RabbitMQConfig("", "", "", "", 15672)
        }
    }
}