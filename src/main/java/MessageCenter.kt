package core

import java.util.*
import kotlin.reflect.jvm.javaType

abstract class MessageCenter{

    val consumers: HashMap<String, ArrayList<EventConsumer<*>>> = HashMap()
    val handlers: HashMap<String, CommandHandler<*>> = HashMap()

    abstract fun send(command: Command)

    abstract fun send(event: Event)



    fun <T: CommandHandler<A>, A: Command> subscribeCommandHandler(handler: T){

        val messageType = handler::class.supertypes[0].arguments[0].type?.javaType?.typeName ?: throw Exception("The event must have a name")

        if(this.handlers.containsKey(messageType))
            throw Exception("This message has a han handler")

        this.handlers[messageType]= handler as CommandHandler<*>
    }

    fun <T: EventConsumer<E>, E: Event> subscribeEventConsumer(consumer: T){

        val messageType = consumer::class.supertypes[0].arguments[0].type?.javaType?.typeName ?: throw Exception("The event must have a name")

        var list = this.consumers.getOrDefault(messageType, ArrayList())

        list.add(consumer as EventConsumer<*>)

        this.consumers[messageType] = list
    }

    protected fun <T: Event> consume(message: T){

        val list = this.consumers.getOrDefault(message.type, ArrayList())

        for (consumer in list){

            val cast = consumer as EventConsumer<T>

            cast.handleIt(message )
        }

    }

    protected fun <T: Command> consume(message: T){

        var handler = this.handlers[message.type]

        val cast = handler as CommandHandler<T>

        cast?.handleIt(message)
    }


}


abstract class Message{
    var id: String = UUID.randomUUID().toString()
    var type: String = this::class.qualifiedName?: throw Exception("No name for a class?")
}


abstract class Event : Message()


abstract class Command: Message()

 abstract class Handler<T: Message>{

    abstract fun handleIt(message: T)

}

abstract class EventConsumer<T : Event>: Handler<T>() {

}


abstract class CommandHandler<T : Command>: Handler<T>(){

}
