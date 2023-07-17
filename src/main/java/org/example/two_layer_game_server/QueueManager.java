package org.example.two_layer_game_server;

import org.example.socket_wrapper.SocketWrapper;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.io.Closeable;
import java.io.IOException;
import java.util.*;

public class QueueManager implements Closeable {
    private Queue<SocketWrapper> queueSocketWrappers = new LinkedList<>();
    private HashSet<SocketWrapper> queueSocketWrappersSet = new HashSet<>();
    private HashSet<SocketWrapper>  gameSocketWrappers = new HashSet<>();

//    private SocketEventListener onPromoted = null;
//    private SocketEventListener onDemoted = null;
//    private SocketEventListener onAdded = null;
//    private SocketEventListener onRemoved = null;

    public QueueManager() {
    }

    @Override
    public void close() throws IOException {
        for(SocketWrapper socketWrapper: queueSocketWrappers) {
            socketWrapper.close();
        }
        for(SocketWrapper socketWrapper: gameSocketWrappers) {
            socketWrapper.close();
        }
    }

    //    public void setOnAdded(SocketEventListener onAdded) {
//        this.onAdded = onAdded;
//    }
//
//    public void setOnRemoved(SocketEventListener onRemoved) {
//        this.onRemoved = onRemoved;
//    }
//
//    public void setOnDemoted(SocketEventListener onDemoted) {
//        this.onDemoted = onDemoted;
//    }
//
//    public void setOnPromoted(SocketEventListener onPromoted) {
//        this.onPromoted = onPromoted;
//    }


    public boolean isInQueue(SocketWrapper socketWrapper) {
        return queueSocketWrappers.contains(socketWrapper);
    }

    public boolean isInGame(SocketWrapper socketWrapper) {
        return gameSocketWrappers.contains(socketWrapper);
    }

    private void addToQueue(SocketWrapper socketWrapper) throws KeyAlreadyExistsException {
        if(isInQueue(socketWrapper)) {
            throw new KeyAlreadyExistsException();
        }

        queueSocketWrappers.add(socketWrapper);
        queueSocketWrappersSet.add(socketWrapper);
    }

    private void addToGame(SocketWrapper socketWrapper) throws KeyAlreadyExistsException {
        if(isInGame(socketWrapper)) {
            throw new KeyAlreadyExistsException();
        }
        gameSocketWrappers.add(socketWrapper);
    }

    private void removeFromGame(SocketWrapper socketWrapper) throws NoSuchElementException {
        if(!isInGame(socketWrapper)) {
            throw new NoSuchElementException();
        }

        gameSocketWrappers.remove(socketWrapper);
    }

    private void removeFromQueue(SocketWrapper socketWrapper) throws NoSuchElementException {
        if(!isInQueue(socketWrapper)) {
            throw new NoSuchElementException();
        }
        queueSocketWrappers.remove(socketWrapper);
        queueSocketWrappersSet.remove(socketWrapper);
    }

    public synchronized void demote(SocketWrapper socketWrapper) throws NoSuchElementException {
        if(!isInGame(socketWrapper)) {
            throw new NoSuchElementException();
        }

        removeFromGame(socketWrapper);
        addToQueue(socketWrapper);
    }

    public synchronized void promote(SocketWrapper socketWrapper) throws NoSuchElementException {
        if(!isInQueue(socketWrapper)) {
            throw new NoSuchElementException();
        }

        removeFromQueue(socketWrapper);
        addToGame(socketWrapper);
    }

    public synchronized void add(SocketWrapper socketWrapper) {
        addToQueue(socketWrapper);
    }
    public synchronized void remove(SocketWrapper socketWrapper) throws IOException {
        if(isInGame(socketWrapper)) {
            demote(socketWrapper);
        }
        if(isInQueue(socketWrapper)) {
            removeFromQueue(socketWrapper);
        }

        socketWrapper.close();
    }

    public synchronized SocketWrapper promoteOne() throws EmptyStackException {
        SocketWrapper socketWrapper = queueSocketWrappers.poll();
        if(socketWrapper == null) {
            throw new EmptyStackException();
        }

        queueSocketWrappersSet.remove(socketWrapper);
        gameSocketWrappers.add(socketWrapper);

        return socketWrapper;
    }

    public synchronized Collection<SocketWrapper> getAllGameSockets() {
        return gameSocketWrappers;
    }

    public synchronized Collection<SocketWrapper> getAllQueueSockets() {
        return queueSocketWrappers;
    }
}
