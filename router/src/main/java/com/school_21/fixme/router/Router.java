package com.school_21.fixme.router;


import java.net.ServerSocket;
import java.net.Socket;

public class Router {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    class BrockerSocker implements Runnable {
        private ServerSocket brokerSocket;

        BrockerSocker(ServerSocket broker) {
            this.brokerSocket = broker;
        }

        public void run() {
            try {
                Socket brokerSocket;
                brokerSocket = new Socket("localhost", 5000);
            } catch (Exception e){

            }
        }
    }
}