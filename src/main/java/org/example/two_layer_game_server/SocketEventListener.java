package org.example.two_layer_game_server;

import org.example.socket_wrapper.SocketWrapper;

public interface SocketEventListener {
    void run(SocketWrapper socket);
}
