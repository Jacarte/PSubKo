import test_domainn.ForeverService
import test_domainn.MessageCenter

import test_domainn.test_domainn.GreetingsEvent
class Process(override var messageCenter: MessageCenter) : ForeverService(){

    override fun registerServices() {
        // Register handlers

        // Do other logics

        while(true){
            messageCenter.send(GreetingsEvent("Pepe"))
            Thread.sleep(5000)
        }
    }

}