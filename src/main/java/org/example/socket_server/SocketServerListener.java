package org.example.socket_server;

import org.example.socket_wrapper.SocketWrapper;

public interface SocketServerListener {
    void onAcceptConnection(SocketWrapper socketWrapper);
}
