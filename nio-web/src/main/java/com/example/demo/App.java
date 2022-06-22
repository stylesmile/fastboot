package com.example.demo;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        Connector connector = new Connector();
        int port = 8081;
        Controller.HOST = "http://localhost:" + port;
        try {
//            connector.test();
            connector.init(port);
            connector.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
