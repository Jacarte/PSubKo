package test_domainn

import test_domainn.impl.MemoryImpl

class ServiceProvider{

    fun getMessageCenter(): MessageCenter {
        return MemoryImpl()
    }

}