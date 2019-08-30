package test_domainn.impl

import test_domainn.Event
import test_domainn.MessageCenter

class MemoryImpl : MessageCenter(){

    override fun send(event: Event) {
        super.consume(event) //To change body of created functions use File | Settings | File Templates.
    }

}