package org.example.socket_server;

import java.net.Socket;
import org.junit.Test;

public class SocketServerTest {
    @Test
    public void test() {
        for (int localPort : new int[]{3000, 5000}) {
            try (
                    Socket clientSocket = new Socket("127.0.0.1", 8080, null, localPort);
            ) {
                System.out.println("Connections created");
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Failed to create a connection");
            }
        }
    }
}
