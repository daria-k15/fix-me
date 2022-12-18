package com.school_21.fixme.broker;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

@Slf4j
public class BrokerThread extends Thread{
    private Socket socket;

    public BrokerThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            while (true){
                Thread.sleep(1000);

            }
        } catch (IOException e) {
            log.error("Got error in Broker: {}", e.getMessage());
        } catch (InterruptedException e){
            log.error("Broker stopped...");
        } finally {
            try {
                socket.close();
            } catch (IOException e){
                log.error("Couldn't stop broker");
            }
        }
    }
}
