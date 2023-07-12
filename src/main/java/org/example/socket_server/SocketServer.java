 package org.example.socket_server;

 import org.example.message.JsonMessage;
 import org.example.socket_wrapper.SocketWrapper;
 import org.example.socket_wrapper.SocketWrapperFactory;

 import javax.management.openmbean.KeyAlreadyExistsException;
 import java.io.*;
 import java.net.ServerSocket;
 import java.net.Socket;
 import java.util.*;
 import java.util.concurrent.BlockingQueue;

 public class SocketServer{
     class IncomingConnectionsListener implements Runnable {
         @Override
         public void run() {
             while(true) {
                 try {
                     Socket socket = serverSocket.accept();
                     SocketWrapper socketWrapper = socketWrapperFactory.getSocketWrapper(socket);
                     onAcceptConnection.onAcceptConnection(socketWrapper);
                 } catch (ImplementationFailsException e) {
                     System.out.println(e);
                 } catch (IOException e) {
                     System.out.println(e);
                 }
             }
         }
     }

     private SocketWrapperFactory socketWrapperFactory = SocketWrapperFactory.getInstance();
     private final int port;
     private final ServerSocket serverSocket;
     private final SocketServerListener onAcceptConnection;
     private Thread incomingConnectionsListenerThread;

     public SocketServer(int port,
                         SocketServerListener onAcceptConnection
                         ) throws IOException {
         this.port = port;
         try {
             this.serverSocket = new ServerSocket(port);
         } catch (IOException e) {
             throw e;
         }
         this.onAcceptConnection = onAcceptConnection;
     }


     public void startAcceptingConnections() {
         this.incomingConnectionsListenerThread = new Thread(new IncomingConnectionsListener());
         incomingConnectionsListenerThread.start();
     }

     public void stopAcceptingConnections() {
         incomingConnectionsListenerThread.interrupt();
     }
 }

 // socket -> added to waiting room -> join game room