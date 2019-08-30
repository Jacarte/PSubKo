import test_domainn.EventConsumer
import test_domainn.test_domainn.GreetingsEvent

class GreetingsHanndler: EventConsumer<GreetingsEvent>() {
    override fun handleIt(message: GreetingsEvent) {
        println("Hello " + message.name)
    }

}