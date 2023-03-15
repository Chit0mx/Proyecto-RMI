package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatClient extends Remote {
    void clientMessage(String message) throws RemoteException;
    void setExecutionTime(long time) throws RemoteException;
}
