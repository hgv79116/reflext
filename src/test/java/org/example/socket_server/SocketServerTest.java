package org.example.socket_server;

import java.io.IOException;
import java.net.Socket;

import org.example.socket_wrapper.SocketWrapper;
import org.junit.Test;

public class SocketServerTest {
    @Test
    public void test() throws IOException {
        SocketServer socketServer = new SocketServer(8080, new SocketServerListener() {
            @Override
            public void onAcceptConnection(SocketWrapper socketWrapper) {
                System.out.println("the server ccepted a connection from " + socketWrapper.getClientAddress() + ":" + socketWrapper.getClientPort());
            }
        });


        for (int localPort : new int[]{3000, 5000}) {
            try (
                    Socket clientSocket = new Socket("127.0.0.1", 8080, null, localPort);
            ) {
                System.out.println("Client: Connections created");
            } catch (Exception e) {
                System.out.println("Client: Failed to create a connection, error: " + e );
            }
        }
    }
}
