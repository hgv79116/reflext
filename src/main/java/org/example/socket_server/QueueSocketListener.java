package org.example.socket_server;

import org.example.message.client_message.ClientMessage;
import org.example.message.MessageListener;

public class QueueSocketListener implements MessageListener<ClientMessage> {
    @Override
    public void onMessage(ClientMessage clientMessage) {
        System.out.println("A qeued player is sending message, ignored lol haha");
    }
}
