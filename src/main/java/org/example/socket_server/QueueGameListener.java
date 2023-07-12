package org.example.socket_server;

import org.example.message.MessageListener;
import org.example.message.server_message.ServerMessage;
import org.example.socket_wrapper.SocketWrapper;

public class QueueGameListener implements MessageListener<ServerMessage> {
    private SocketWrapper socketWrapper;
    public QueueGameListener(SocketWrapper socketWrapper) {
        this.socketWrapper = socketWrapper;
    }
    @Override
    public void onMessage(ServerMessage serverMessage) throws ImplementationFailsException {
        System.out.println("skipping this one lol");
    }
}
