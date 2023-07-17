package org.example.socket_wrapper;

import org.example.message.client_message.ClientMessage;
import org.example.message.MessageListener;
import org.example.message.server_message.HeartbeatMessage;
import org.example.message.server_message.ServerMessage;
import org.example.socket_server.ImplementationFailsException;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class SocketWrapper implements Closeable {
    private final long TIMEOUT = 4000;
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

    @Override
    public void close() throws IOException{
        System.out.println("Closed connection from " + socket.getInetAddress() + socket.getPort());
        stopListening();
        socket.close();
        printStream.close();
        bufferedReader.close();

        listeningThread.interrupt();
        try {
            listeningThread.join();
        } catch (Exception e) {
            System.out.println("closing socket wrapper met with exception " + e);
        }
    }

    public synchronized void sendMessage(ServerMessage message) {
        printStream.println(message.toString());
    }

    public synchronized void addListener(MessageListener<ClientMessage> messageListener) {
        messageListeners.add(messageListener);
    }

    public synchronized void removeListener(MessageListener<ClientMessage> messageListener) {
        messageListeners.remove(messageListener);
    }

    public synchronized void startListening() throws ImplementationFailsException {
        listeningThread = new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder currentLine = new StringBuilder();

                while (!listeningStopped) {
                    try {
                        if(bufferedReader.ready()) {
                            int c = bufferedReader.read();
                            if(c == '\n') {
                                String line = currentLine.toString();
                                System.out.println("line: " + line);
                                currentLine = new StringBuilder();

                                ClientMessage message = new ClientMessage(line);

                                for (MessageListener<ClientMessage> messageListener : messageListeners) {
                                    try {
                                        messageListener.onMessage(message);
                                    } catch (Exception e) {
                                        System.out.println(e);
                                    }
                                }
                            }
                            else {
                                currentLine.append((char)c);
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                        break;
                    }
                }
            }
        });
        listeningThread.start();
    }

    public synchronized void stopListening() {
        listeningStopped = true;
    }

//    public void ping(Runnable onConected, Runnable onDisconnected) {
//        long pingTime = System.currentTimeMillis();
//        sendMessage(new HeartbeatMessage(pingTime));
//
//        while(true) {
//            long currentTime = System.currentTimeMillis();
//            if (currentTime >= TIMEOUT + pingTime) {
//                onDisconnected.run();
//                return;
//            }
//            if(bufferedReader)
//        }
//    }

    public boolean checkDisconnected() {
        return false;
    }

    public InetAddress getClientAddress() {
        return socket.getInetAddress();
    }

    public int getClientPort() {
        return socket.getPort();
    }
}
