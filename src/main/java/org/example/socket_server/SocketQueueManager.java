package org.example.socket_server;

import java.net.Socket;
import java.util.EmptyStackException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import javax.management.openmbean.KeyAlreadyExistsException;

public class SocketQueueManager implements SocketManager{
    Queue<Socket> queueSocket; 
    HashSet<Socket> queueSocketsSet; 
    
    HashSet<Socket> gameSockets; 

    public SocketQueueManager() {
        this.queueSocket = new LinkedList<>(); 
        this.queueSocketsSet = new HashSet<>(); 
        this.gameSockets = new HashSet<>(); 
    }

    public boolean isInQueue(Socket socket) { 
        return queueSocket.contains(socket); 
    }
        
    public boolean isInGame(Socket socket) { 
        return gameSockets.contains(socket); 
    }
    
    private void addToQueue(Socket socket) throws KeyAlreadyExistsException{ 
        if(isInQueue(socket)) { 
            throw new KeyAlreadyExistsException(); 
        }

        queueSocket.add(socket); 
        queueSocketsSet.add(socket); 
    }

    private void addToGame(Socket socket) throws KeyAlreadyExistsException { 
        if(isInGame(socket)) { 
            throw new KeyAlreadyExistsException(); 
        }
        gameSockets.add(socket); 
    }

    private void removeFromGame(Socket socket) throws NoSuchElementException { 
        if(!isInGame(socket)) {
            throw new NoSuchElementException(); 
        }

        gameSockets.remove(socket);;
    }

    private void removeFromQueue(Socket socket) throws NoSuchElementException { 
        if(!isInQueue(socket)) { 
            throw new NoSuchElementException();
        }
        queueSocket.remove(socket); 
        queueSocketsSet.remove(socket); 
    }

    public synchronized void demote(Socket socket) throws NoSuchElementException { 
        if(!isInGame(socket)) { 
            throw new NoSuchElementException(); 
        }

        removeFromGame(socket); 
        addToQueue(socket);
    }

    public synchronized void promote(Socket socket) throws NoSuchElementException { 
        if(!isInQueue(socket)) { 
            throw new NoSuchElementException(); 
        }

        removeFromQueue(socket);
        addToGame(socket);
    }

    public synchronized void add(Socket socket) { 
        addToQueue(socket);
    }
    public synchronized void remove(Socket socket) { 
        if(isInGame(socket)) { 
            demote(socket); 
        }
        if(isInQueue(socket)) {
            removeFromQueue(socket);
        }
    }

    public synchronized void promoteOne() throws EmptyStackException { 
        Socket socket = queueSocket.poll(); 
        if(socket == null) { 
            throw new EmptyStackException(); 
        }

        queueSocketsSet.remove(socket); 
        gameSockets.add(socket); 
    }
}
