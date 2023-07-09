package org.example;

import org.example.socket_server.SocketServerTest;

public class Main {
    public static void main(String[] args) {
        System.out.println("Test started");
        SocketServerTest socketServerTest = new SocketServerTest();
        socketServerTest.test();
    }
}
