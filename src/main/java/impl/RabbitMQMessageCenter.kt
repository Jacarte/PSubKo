package impl
import core.*


class RabbitMQMessageCenter(val connectionString: String, val user: String, val password: String): MessageCenter() {
    override fun send(event: Event) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun send(command: Command) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}