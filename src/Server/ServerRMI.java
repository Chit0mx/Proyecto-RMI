package Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerRMI {
    public static void main(String[] args) {
        System.setProperty("java.rmi.server.hostname", "192.168.100.8");

        try {
            Registry rmi = LocateRegistry.createRegistry(1005);

            //user
            rmi.rebind("cipher", new ServerImplementation());
            System.out.println("Servidor activo...");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
