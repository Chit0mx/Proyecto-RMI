package Client;

import Interfaces.ChatClient;
import Interfaces.ChatServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientImplementation extends UnicastRemoteObject implements ChatClient {
    public final ChatServer server;
    public final String name;
    public String cipheredText;
    public long time;

    public ClientImplementation(String name, ChatServer server) throws RemoteException {
        this.server = server;
        this.name = name;

        server.register(name,this);
    }

    /**
     *
     * @param message ciphered string
     */
    @Override
    public void clientMessage(String message) throws RemoteException {
        //Receives ciphered string and writes it to output file.
        cipheredText = message;

        System.out.println("Text received\n");
    }

    @Override
    public void setExecutionTime(long time) throws RemoteException {
        this.time = time;
        System.out.println(time + " ms\n");
    }
}
