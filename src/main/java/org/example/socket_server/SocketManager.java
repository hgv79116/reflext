package org.example.socket_server;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.net.Socket;
import java.util.EmptyStackException;
import java.util.NoSuchElementException;

public interface SocketManager {
    boolean isInQueue(Socket socket);
    boolean isInGame(Socket socket);
    void add(Socket socket) throws KeyAlreadyExistsException;
    void remove(Socket socket) throws NoSuchElementException;
    void promote(Socket socket) throws NoSuchElementException;
    void demote(Socket socket) throws NoSuchElementException;
    void promoteOne() throws EmptyStackException;
}
