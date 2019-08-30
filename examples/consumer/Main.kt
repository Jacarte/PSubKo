import impl.RabbitMQMessageCenter
import java.lang.Integer.parseInt

fun main(){

    val vHost = System.getenv("RMQ_VIRTUAL_HOST")?:"/"
    val host = System.getenv("RMQ_HOST")?:"localhost"
    val user = System.getenv("RMQ_USER")?:"guest"
    val pass = System.getenv("RMQ_PASSWORD")?:"guest"
    val port = parseInt(System.getenv("RMQ_PORT")?:"5672")



    Process(RabbitMQMessageCenter(vHost,host,user,pass,port))
        .main()
}