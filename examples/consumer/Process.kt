import test_domainn.ForeverService
import test_domainn.MessageCenter

class Process(override var messageCenter: MessageCenter) : ForeverService(){

    override fun registerServices() {
        // Register handlers
        messageCenter.subscribeEventConsumer(GreetingsHanndler())

        // Do other logics
    }

}