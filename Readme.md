Lightweight pub/sub architecture in Kotlin (PSubKo)
===

This project is the core to create separate computing nodes inside a pub/sub architecture. There are two implementations for message dispatching, one using naive in-memory implementation and another using RabbitMQ client interface. It means to be lightweight. The core package is implemented in Kotlin. 

Create a Node
===

1. Create an empty project and add the package in this repository as a dependency
2. Create a class for the listener process, i.e. the forever loop that will listen to system messages. You must provide the implementation for the ```registerServices``` method, inside the body of the function you should register your event consumers. The example below illustrates and example.

```kt
import test_domainn.ForeverService
import test_domainn.MessageCenter

import test_domainn.test_domainn.GreetingsEvent
class Process(override var messageCenter: MessageCenter) : ForeverService(){

    
    override fun registerServices() {
        // Register handlers
        messageCenter.subscribeEventConsumer(GreetingsHanndler())

        // Do other logics
    }

}

```

3. Instantiate the previous declared class in the entrypoint of your application, and then call the main function. This action will create a deadlock with the Java main thread preventing your application to get closed.

```kt
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

```

4. Start to send and process events !!

Key insights
===

- **Java main thread hack** To prevent application closing without a forever loop (like while(true){}), we call ```Thread.currentThread().join()``` in the main thread, doing this, the main thread will wait for the main thread itself, providing a "good-for-us" deadlock. 
- **Json encoding of messages** (RabbitMQ) Every sent message is encoded to a json representation. We put the canonical Java class name in a header called ```ClassType```. In this way we achieve the serialization of the message in the correct type, and then the consequently handler call.

Examples
===
In this [folder](examples), we put a concrete example using this architecture: one node wich produce messages and another that process them. 


#### Docker compose example

This is an example to deply this services in docker as a stack of services.


**docker-compose.yml**

```yml

version: '3'
services:
 rabbitmq:
  restart: always
  image: rabbitmq:3-management
 rabbitmq_test2833:
  build: ./examples/rabbitmq_test
  restart: always
  environment:
   RMQ_VIRTUAL_HOST: /
   RMQ_HOST: rabbitmq
   RMQ_USER: guest
   RMQ_PASSWORD: guest
   RMQ_PORT: 5672
  depends_on:
   - rabbitmq
  links:
   - rabbitmq
 rabbitmq_producer4468:
  build: ./examples/rabbitmq_producer
  restart: always
  environment:
   RMQ_VIRTUAL_HOST: /
   RMQ_HOST: rabbitmq
   RMQ_USER: guest
   RMQ_PASSWORD: guest
   RMQ_PORT: 5672
  depends_on:
   - rabbitmq
  links:
   - rabbitmq

```

**Dockerfile**

```
FROM anapsix/alpine-java
COPY ./bin.jar ./bin.jar
CMD ["java", "-jar", "bin.jar"]
```
