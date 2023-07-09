 package org.example.socket_server;

 import org.example.message.JsonMessage;
 import org.example.message.Message;
 import org.json.JSONObject;

 import java.io.*;
 import java.net.ServerSocket;
 import java.net.Socket;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.Set;
 import java.util.concurrent.BlockingQueue;

 public class SocketServer {
     class ClientSocketWrapper {
         private Socket clientSocket;
         private PrintStream printStream;
         private BufferedReader bufferedReader;
     }
     class IncomingConnectionsListener implements Runnable {
         @Override
         public void run() {
             while(true) {
                 try {
                     Socket clientSocket = serverSocket.accept();
                     Thread clientListener = new Thread(new ClientSocketListener(
                             clientSocket,
                             outputMessageQueue
                     ));
                     clientSocketListeners.put(clientSocket, clientListener);
                     clientListener.start();
                 } catch (IOException e) {
                     System.out.println(e);
                 }
             }
         }
     }

    public boolean isAlive() {
        return true;
    }


     }

     class ClientSocketAnnouncer implements Runnable{
         @Override
         public void run() {
             while(true) {
                 try {
                     JsonMessage jsonMessage = inputMessageQueue.take();
                     for(Socket clientSocket: clientSocketListeners.keySet()) {
                         clientSocket.getOutputStream().
                     }
                 } catch (Exception e) {
                     System.out.println("Error!!!!" + e);
                 }
             }
         }
     }

     class ClientSocketListener implements  Runnable {
         private final Socket clientSocket;
         private final BufferedReader bufferedReader;
         private final BlockingQueue<JsonMessage> outputMessageQueue;
         public ClientSocketListener(
                 Socket clientSocket,
                 BlockingQueue<JsonMessage> outputMessageQueue) throws IOException {
             try {
                 this.clientSocket = clientSocket;
                 this.bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 this.outputMessageQueue = outputMessageQueue;
             } catch (IOException e) {
                 throw e;
             }
         }

         private JsonMessage readJsonMesssage() throws IOException{
             String line = bufferedReader.readLine();
             return new JsonMessage(new JSONObject(line));
         }

         @Override
         public void run() {
             while(true) {
                 try {
                     JsonMessage jsonMessage = readJsonMesssage();
                     outputMessageQueue.put(jsonMessage);
                 } catch (IOException e) {
                     System.out.println("Error!!!!!" + e);
                 } catch (InterruptedException e) {
                     System.out.println("Interrupted while writing message to queue: " + e);
                 }
             }
         }
     }
     private final BlockingQueue<JsonMessage> inputMessageQueue;
     private final BlockingQueue<JsonMessage> outputMessageQueue;
     private final int port;

     private final ServerSocket serverSocket;
     private Thread incomingConnectionsListenerThread;
     private HashMap<Socket, Thread> clientSocketListeners;

     private final int maxConnection = 10;

     public SocketServer(BlockingQueue<JsonMessage> inputMessageQueue,
                  BlockingQueue<JsonMessage> outputMessageQueue,
                  int port) throws IOException {
         this.inputMessageQueue = inputMessageQueue;
         this.outputMessageQueue = outputMessageQueue;
         this.port = port;
         try {
             this.serverSocket = new ServerSocket(port);
         } catch (IOException e) {
             throw e;
         }
     }


     private void startAcceptingConnections() {
         this.incomingConnectionsListenerThread = new Thread(new IncomingConnectionsListener());
         incomingConnectionsListenerThread.start();
     }

     private void stopAcceptingConnections() {
         incomingConnectionsListenerThread.interrupt();
     }


     public void start() {
         startAcceptingConnections();
     }

     private void addToQueue(Socket socket) {
        
     }

     private

     private void removeFromQueueSocket(Socket socket) {

     }

     private void
 }

 // socket -> added to waiting room -> join game room