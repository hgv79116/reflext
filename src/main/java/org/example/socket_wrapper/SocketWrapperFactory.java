package org.example.socket_wrapper;

import org.example.socket_server.ImplementationFailsException;

import java.net.Socket;
import java.util.HashMap;

// this class should also be singleton
public class SocketWrapperFactory {
    private static SocketWrapperFactory instance;
    private HashMap<Socket, SocketWrapper> socketWrapperHashMap;
    private SocketWrapperFactory() {
        socketWrapperHashMap = new HashMap<>();
    }
    public static SocketWrapperFactory getInstance() {
        if(instance == null) {
            instance = new SocketWrapperFactory();
        }
        return instance;
    }

    //Factory, Singleton?
    public SocketWrapper getSocketWrapper(Socket socket) throws ImplementationFailsException {
        if(socketWrapperHashMap.get(socket) == null) {
            socketWrapperHashMap.put(socket, new SocketWrapper(socket));
        }
        return socketWrapperHashMap.get(socket);
    }
}
