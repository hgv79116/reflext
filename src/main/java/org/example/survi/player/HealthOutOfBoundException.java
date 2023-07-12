package org.example.survi.player;

public class HealthOutOfBoundException extends Exception{
    public HealthOutOfBoundException(String errorMessage) {
        super(errorMessage);
    }

    public HealthOutOfBoundException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
