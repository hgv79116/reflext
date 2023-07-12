package org.example.socket_server;

public class ImplementationFailsException extends Exception{
    public ImplementationFailsException(String message) {
        super(message);
    }
    public ImplementationFailsException(String message, Throwable error) {
        super(message, error);
    }
}
