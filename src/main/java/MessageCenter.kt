package test_domainn

import java.util.*
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.jvmName

abstract class MessageCenter{

    val consumers: HashMap<String, ArrayList<EventConsumer<*>>> = HashMap()


    abstract fun send(event: Event)


    fun <T: EventConsumer<E>, E: Event> subscribeEventConsumer(consumer: T){

        println("Subscribing...%s".format(consumer::class.jvmName))

        val messageType = consumer::class.supertypes[0].arguments[0].type?.javaType?.typeName ?: throw Exception("The event must have a name")

        var list = this.consumers.getOrDefault(messageType, ArrayList())

        list.add(consumer as EventConsumer<*>)

        this.consumers[messageType] = list
    }

    protected fun <T: Event> consume(message: T): Boolean{

        val list = this.consumers.getOrDefault(message.type, ArrayList())

        var processed = false

        for (consumer in list){

            val cast = consumer as EventConsumer<T>

            cast.handleIt(message )

            processed = true
        }

        return processed

    }

}


abstract class Message{
    var id: String = UUID.randomUUID().toString()
    var type: String = this::class.qualifiedName?: throw Exception("No name for a class?")
}


abstract class Event : Message()


 abstract class Handler<T: Message>{

    abstract fun handleIt(message: T)

}

abstract class EventConsumer<T : Event>: Handler<T>()
