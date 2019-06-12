package com.company;

import com.company.server.HttpServer;

public class Main {

    public static void main(String[] args) {
        HttpServer server = new HttpServer();
        server.await();
    }
}
