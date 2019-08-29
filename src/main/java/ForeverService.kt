package core

import java.lang.Exception

abstract class ForeverService {

    abstract var messageCenter: MessageCenter

    abstract fun registerServices();

    fun main(){

        println("Opening service...")

        try {

            registerServices()

            Thread.currentThread().join() // hack to deadlock main thread
        }
        catch(e: Exception){
            println("Closing..." + e.message)
        }


    }

}