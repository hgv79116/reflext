package org.example.socket_wrapper;

import org.example.message.client_message.ClientMessage;
import org.example.message.MessageListener;
import org.example.message.server_message.ServerMessage;
import org.example.socket_server.ImplementationFailsException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

public class SocketWrapper {
    private final Socket socket;
    private final PrintStream printStream;
    private final BufferedReader bufferedReader;
    private ArrayList<MessageListener<ClientMessage>> messageListeners;
    private Thread listeningThread;
    private volatile boolean listeningStopped;
    public SocketWrapper(Socket socket) throws ImplementationFailsException {
        this.socket = socket;
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.printStream = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new ImplementationFailsException(e.getMessage(), e.getCause());
        }
        this.messageListeners = new ArrayList<>();
    }
    public void sendMessage(ServerMessage message) {
        printStream.println(message.toString());
    }

    public void addListener(MessageListener<ClientMessage> messageListener) {
        messageListeners.add(messageListener);
    }

    public void removeListener(MessageListener<ClientMessage> messageListener) {
        messageListeners.remove(messageListener);
    }

    public void startListening() throws ImplementationFailsException {
        listeningThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!listeningStopped) {
                    try {
                        String line = bufferedReader.readLine();
                        ClientMessage message = new ClientMessage(line);
                        for (MessageListener<ClientMessage> messageListener : messageListeners) {
                            messageListener.onMessage(message);
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }

                }
            }
        });
        listeningThread.start();
    }

    public void stopListening() {
        listeningStopped = true;
    }

    public boolean checkDisconnected() {
        return false; // to be implemented
    }
}
