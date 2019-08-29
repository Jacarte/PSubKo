package core

import core.impl.MemoryImpl

class ServiceProvider{

    fun getMessageCenter(): MessageCenter {
        return MemoryImpl()
    }

}