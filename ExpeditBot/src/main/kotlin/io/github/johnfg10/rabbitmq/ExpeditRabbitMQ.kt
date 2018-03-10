package io.github.johnfg10.rabbitmq

import com.rabbitmq.client.*
import org.apache.commons.logging.LogFactory
import java.nio.charset.Charset

class ExpeditRabbitMQ(private val rabbitMQConfig: RabbitMQConfig) {
    private val connection: Connection
    private val channel: Channel

    init {
        val conFactory = ConnectionFactory()
        conFactory.username = rabbitMQConfig.username
        conFactory.password = rabbitMQConfig.password
        conFactory.virtualHost = rabbitMQConfig.virtualHost
        conFactory.port = rabbitMQConfig.port
        conFactory.host = rabbitMQConfig.hostname
        connection = conFactory.newConnection()
        channel = connection.createChannel()
        channel.addShutdownListener {
            log.error("is hard error: ${it.isHardError} reason: ${it.reason}")
        }

        channel.exchangeDeclare(rabbitMQConfig.exchange, BuiltinExchangeType.FANOUT)

        channel.queueDeclare(rabbitMQConfig.queue , true, false, false, null)

        channel.queueBind(rabbitMQConfig.queue, rabbitMQConfig.exchange, rabbitMQConfig.routingKey)

        channel.basicConsume(rabbitMQConfig.queue, true, "basicDeliveryHandler",
                object : DefaultConsumer(channel) {
                    override fun handleDelivery(consumerTag: String,
                                                envelope: Envelope,
                                                properties: AMQP.BasicProperties,
                                                body: ByteArray) {
                        if (properties.contentType == "text/plain")
                            println("rabbitMQ received \"" + body.toString(Charset.forName(properties.contentEncoding)) + "\"")
                    }
                })


    }

    public fun sendMessage(msges: String){
        channel.basicPublish(rabbitMQConfig.exchange, rabbitMQConfig.routingKey,
                AMQP.BasicProperties.Builder().contentType("text/plain")
                        .contentEncoding("UTF-8").build(), msges.toByteArray())
    }

/*    private fun requestMessage(callback: () -> Unit) {
        callback()
    }*/

    companion object {
        private val log = LogFactory.getLog(ExpeditRabbitMQ::class.java)
    }
}