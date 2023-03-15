package Client;

import Cipher.CaesarCipher;
import Interfaces.ChatServer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;

public class CLIENT extends JFrame implements ActionListener {
    public static JTextField txtOffset;
    public static JTextArea txtInput;
    public static JTextArea txtOutput;

    public static JLabel txtCant;
    public static JLabel txtSO;
    public static JLabel txtOr;
    public static JLabel secTime;
    public static JLabel fyTime;
    public static JLabel esTime;
    public static JButton btnCifrar;
    public static JButton btnForkJoin;
    public static JButton btnExecutorService;
    public static JButton btnSendMessage;
    public static JButton btnSendOffset;
    public static JButton btnReset;
    static ClientImplementation clientImplementation;

    public CLIENT() {
        setTitle("Merge Sort 19310170");
        setSize(1280, 720);
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        txtCant = new JLabel("Desplazamiento");
        txtCant.setBounds(50, 0, 350, 50);

        txtOffset = new JTextField();
        txtOffset.setBounds(50, 50, 150, 50);

        btnCifrar = new JButton("Secuencial");
        btnCifrar.setBounds(210, 50, 100, 50);
        btnCifrar.setActionCommand("cifrar");
        btnCifrar.addActionListener(this);

        btnForkJoin = new JButton("ForkJoin");
        btnForkJoin.setBounds(330, 50, 100, 50);
        btnForkJoin.setActionCommand("forkjoin");
        btnForkJoin.addActionListener(this);

        btnExecutorService = new JButton("Executor");
        btnExecutorService.setBounds(450, 50, 100, 50);
        btnExecutorService.setActionCommand("executor");
        btnExecutorService.addActionListener(this);

        btnSendMessage = new JButton("Mensaje");
        btnSendMessage.setBounds(570, 50, 100, 50);
        btnSendMessage.setActionCommand("message");
        btnSendMessage.addActionListener(this);

        btnSendOffset = new JButton("Offset");
        btnSendOffset.setBounds(690, 50, 100, 50);
        btnSendOffset.setActionCommand("offset");
        btnSendOffset.addActionListener(this);

        btnReset = new JButton("Limpiar");
        btnReset.setBounds(810, 50, 100, 50);
        btnReset.setActionCommand("reset");
        btnReset.addActionListener(this);

        txtSO = new JLabel("Texto original:");
        txtSO.setBounds(50, 100, 150, 50);

        txtInput = new JTextArea(20, 100);
        txtInput.setLineWrap(true);
        txtInput.setEditable(true);

        JScrollPane scrollInput = new JScrollPane(txtInput);
        scrollInput.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollInput.setBounds(50, 150, 1180, 225);

        txtOr = new JLabel("Texto cifrado:");
        txtOr.setBounds(50, 370, 150, 50);

        txtOutput = new JTextArea(20, 100);
        txtOutput.setLineWrap(true);
        txtOutput.setEditable(false);

        JScrollPane scrollOutput = new JScrollPane(txtOutput);
        scrollOutput.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollOutput.setBounds(50, 420, 1180, 225);

        secTime = new JLabel("Secuencial");
        secTime.setBounds(50, 640, 250, 50);

        fyTime = new JLabel("Fork Join");
        fyTime.setBounds(200, 640, 250, 50);

        esTime = new JLabel("Executor Services");
        esTime.setBounds(350, 640, 250, 50);

        this.add(txtOffset);
        this.add(txtCant);
        this.add(secTime);
        this.add(fyTime);
        this.add(esTime);
        this.add(btnCifrar);
        this.add(btnForkJoin);
        this.add(btnExecutorService);
        this.add(btnSendMessage);
        this.add(btnSendOffset);
        this.add(btnReset);
        this.add(scrollInput);
        this.add(scrollOutput);
        this.add(txtSO);
        this.add(txtOr);

        setVisible(true);

        this.setVisible(true);
    }

    public static void main(String[] args) {
        new CLIENT();

        String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        System.out.println(timestamp);

        try {
            Registry rmi = LocateRegistry.getRegistry("192.168.100.8", 1005);

            ChatServer server = (ChatServer) rmi.lookup("cipher");
            clientImplementation = new ClientImplementation(timestamp, server);

        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String inputString = txtInput.getText();

        switch (e.getActionCommand()) {
            case "message" -> {
                    try {
                        clientImplementation.server.receiveMessage(inputString);
                        System.out.println("Text sent\n");
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
            }

            case "offset" -> {
                int offset = Integer.parseInt(txtOffset.getText());

                try {
                    clientImplementation.server.getOffset(offset);
                    System.out.println("Offset sent\n");
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }

            case "reset" -> {
                try {
                    clientImplementation.server.reset();
                    System.out.println("Text reset\n");
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }

            case "cifrar" -> {
                try {
                    clientImplementation.server.startCipher(1, clientImplementation.name);
                    secTime.setText("Secuencial: " + clientImplementation.time);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }

            case "forkjoin" -> {
                try {
                    clientImplementation.server.startCipher(2, clientImplementation.name);
                    fyTime.setText("Fork Join: " + clientImplementation.time);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }

            case "executor" -> {
                try {
                    clientImplementation.server.startCipher(3, clientImplementation.name);
                    esTime.setText("Executor Services: " + clientImplementation.time);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        txtOutput.setText(clientImplementation.cipheredText);
    }
}
