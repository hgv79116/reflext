package org.example.socket_server;

import org.example.message.JsonMessage;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.net.Socket;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;

public class SocketMessageManager implements SocketManager {
    private final BlockingQueue<JsonMessage> inputMessageQueue;
    private final BlockingQueue<JsonMessage> outputMessageQueue;

    private SocketQueueManager socketQueueManager;



    public SocketMessageManager(
            BlockingQueue<JsonMessage> inputMessageQueue,
            BlockingQueue<JsonMessage> outputMessageQueue) {
        this.socketQueueManager = new SocketQueueManager();
        this.inputMessageQueue = inputMessageQueue;
        this.outputMessageQueue = outputMessageQueue;
    }

    private void addToQueue(Socket socket) {

    }

    private void removeFromQueue(Socket socket) {

    }

    private void addToGame(Socket socket) {

    }

    private void removeFromGame(Socket socket) {

    }

    @Override
    public boolean isInGame(Socket socket) {
        return socketQueueManager.isInGame(socket);
    }

    @Override
    public boolean isInQueue(Socket socket) {
        return socketQueueManager.isInQueue(socket);
    }

    @Override
    public void add(Socket socket) throws KeyAlreadyExistsException {
        socketQueueManager.add(socket);

    }

    @Override
    public void remove(Socket socket) throws NoSuchElementException {

    }

    @Override
    public void demote(Socket socket) throws NoSuchElementException {

    }

    @Override
    public void promote(Socket socket) throws NoSuchElementException {

    }

    @Override
    public void promoteOne() throws EmptyStackException {

    }
}
