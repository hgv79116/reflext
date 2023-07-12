package org.example.message;

import org.example.socket_server.ImplementationFailsException;

public interface MessageListener<T extends Message> {
    void onMessage(T message) throws ImplementationFailsException;
}
