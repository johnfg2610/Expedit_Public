package io.github.johnfg10

import java.rmi.Remote
import java.rmi.RemoteException

interface IExpeditCommunication : Remote {
    @Throws(RemoteException::class)
    fun getHealthInfo() : Health


}