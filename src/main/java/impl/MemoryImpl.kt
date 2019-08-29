package core.impl

import core.Event
import core.MessageCenter

class MemoryImpl : MessageCenter(){

    override fun send(event: Event) {
        super.consume(event) //To change body of created functions use File | Settings | File Templates.
    }

}