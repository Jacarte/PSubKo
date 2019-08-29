package impl
import com.google.gson.Gson
import com.rabbitmq.client.*
import core.*
import org.w3c.dom.Node
import java.lang.Exception
import kotlin.reflect.KClass


class RabbitMQMessageCenter(virtualHost: String, connectionString: String, user: String, password: String, port: Int): MessageCenter() {


    val connection: Connection
    val channel: Channel
    val routinKey: String = "micro_node_message"
    val exchangeName: String = "micro_node"
    var queueName: String = "events"

    init{



        val factory = ConnectionFactory()
        factory.username = user
        factory.password = password
        factory.host = connectionString
        factory.port = port
        factory.virtualHost = virtualHost

        connection = factory.newConnection()
        channel = connection.createChannel()


        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT)


        queueName = channel.queueDeclare().queue

        channel.queueBind(queueName, exchangeName, routinKey)


        channel.basicConsume(queueName, true, NodeConsumer(channel, this))
    }

    override fun send(event: Event) {


        val headers = HashMap<String, Any>()
        headers["ClassName"] = event.type

        val properties = AMQP.BasicProperties().builder().headers(headers).build()

        channel.basicPublish(exchangeName, routinKey, properties, Gson().toJson(event).toByteArray())
    }


    class NodeConsumer(channel: Channel, val messageCenter: RabbitMQMessageCenter): DefaultConsumer(channel){
        override fun handleDelivery(
            consumerTag: String?,
            envelope: Envelope?,
            properties: AMQP.BasicProperties?,
            body: ByteArray?
        ) {

            val routingKey = envelope?.routingKey
            val contentType = properties?.contentType
            val deliveryTag = envelope?.deliveryTag?: throw Exception("No delivery tag")


            val clazz = ClassLoader.getSystemClassLoader().loadClass(properties?.headers!!["ClassName"].toString())

            val type = Gson().fromJson(String(body?: throw Exception("No message body?")), clazz)

            messageCenter.consume(type as Event)


        }
    }
}