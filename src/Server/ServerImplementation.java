package Server;

import Cipher.CaesarCipher;
import Interfaces.ChatClient;
import Interfaces.ChatServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.*;

public class ServerImplementation extends UnicastRemoteObject implements ChatServer {
    public List<ChatClient> clients;
    public Map<String, ChatClient> clientMap;
    private String string;
    private int offset;
    private boolean messageFlag, offsetFlag;

    public ServerImplementation() throws RemoteException {
        messageFlag = false;
        offsetFlag = false;
        clients = new ArrayList<>();
        clientMap = new HashMap<>();
        this.string = "";
    }

    @Override
    public void register(String name, ChatClient client) throws RemoteException {
        this.clients.add(client);
        this.clientMap.put(name, client);
        System.out.println("Client added\n");
    }

    @Override
    public void receiveMessage(String message) throws RemoteException {
        this.string = this.string + " " + message;
        messageFlag = true;
        System.out.println("Message received\n");
    }

    @Override
    public void getOffset(int offset) throws RemoteException {
        this.offset = offset;
        offsetFlag = true;
        System.out.println("Offset received\n");
    }

    @Override
    public void reset() throws RemoteException {
        this.string = "";
    }

    @Override
    public void sendMessage(String finalString, String name) throws RemoteException {
//        for (ChatClient client : clients) {
//            client.clientMessage(finalString);
//        }

        clientMap.get(name).clientMessage(finalString);
        System.out.println("Ciphered message sent\n");
    }

    @Override
    public void sendExecutionTime(long time) throws RemoteException {
        for (ChatClient client : clients) {
            client.setExecutionTime(time);
        }
        System.out.println("Execution time sent\n");
    }

    @Override
    public void startCipher(int type, String name) throws RemoteException {
        if (messageFlag && offsetFlag) {
            String finalString;
            long startTime, endTime;

            switch (type) {
                case 1 -> {
                    //Sequential
                    startTime = System.nanoTime();

                    finalString = CaesarCipher.cipher(string, offset);
                    sendMessage(finalString, name);

                    endTime = System.nanoTime();
                    sendExecutionTime((endTime - startTime) / 1000000);
                }

                case 2 -> {
                    //Fork Join
                    startTime = System.nanoTime();

                    ForkJoinPool pool = new ForkJoinPool();
                    CaesarCipher caesarCipher = new CaesarCipher(string, offset);

                    finalString = pool.invoke(caesarCipher);

                    sendMessage(finalString, name);

                    endTime = System.nanoTime();
                    sendExecutionTime((endTime - startTime) / 1000000);
                }

                case 3 -> {
                    //ExecutorService
                    startTime = System.nanoTime();

                    try {
                        StringBuilder stringBuilder = new StringBuilder();
                        int third = string.length() / 3;
                        ExecutorService executorService = Executors.newFixedThreadPool(10);
                        Collection<Callable<String>> callables = new ArrayList<>();

                        callables.add(() -> CaesarCipher.cipher(String.valueOf(string).substring(0, third), offset));
                        callables.add(() -> CaesarCipher.cipher(String.valueOf(string).substring(third, third * 2), offset));
                        callables.add(() -> CaesarCipher.cipher(String.valueOf(string).substring(third * 2), offset));

                        List<Future<String>> futures = executorService.invokeAll(callables);

                        for (Future<String> future : futures) {
                            stringBuilder.append(future.get());
                        }

                        sendMessage(stringBuilder.toString(), name);

                        endTime = System.nanoTime();
                        sendExecutionTime((endTime - startTime) / 1000000);
                    } catch (ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
    }

}
