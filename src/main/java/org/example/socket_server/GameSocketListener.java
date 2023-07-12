package org.example.socket_server;

import org.example.message.client_message.ClientMessage;
import org.example.message.JsonMessage;
import org.example.message.MessageListener;

import java.util.concurrent.BlockingQueue;

public class GameSocketListener implements MessageListener<ClientMessage> {
    private BlockingQueue<JsonMessage> inputMessageQueue;
    public GameSocketListener(BlockingQueue<JsonMessage> inputMessageQueue) {
        this.inputMessageQueue = inputMessageQueue;
    }
    @Override
    public void onMessage(ClientMessage clientMessage) throws ImplementationFailsException{
        try {
            inputMessageQueue.put(clientMessage);
        } catch (InterruptedException e) {
            throw new ImplementationFailsException(e.getMessage(), e.getCause());
        }
    }
}
