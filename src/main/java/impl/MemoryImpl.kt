package core.impl

import core.Command
import core.Event
import core.MessageCenter

class MemoryImpl : MessageCenter(){

    override fun send(command: Command) {
        super.consume(command) //To change body of created functions use File | Settings | File Templates.
    }

    override fun send(event: Event) {
        super.consume(event) //To change body of created functions use File | Settings | File Templates.
    }

}