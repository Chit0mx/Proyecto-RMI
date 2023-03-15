package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServer extends Remote {
    void register(String name, ChatClient client) throws RemoteException;
    void receiveMessage(String message) throws RemoteException;
    void getOffset(int offset) throws RemoteException;
    void sendMessage(String finalString, String name) throws RemoteException;
    void sendExecutionTime(long time) throws RemoteException;
    void startCipher(int type, String name) throws RemoteException;

    void reset() throws RemoteException;
}
