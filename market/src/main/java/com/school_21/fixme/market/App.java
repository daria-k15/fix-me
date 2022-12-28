package com.school_21.fixme.market;

import com.school_21.fixme.market.markets.CryptoMarket;
import com.school_21.fixme.utils.messages.Message;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

public class App {
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;
    private static CryptoMarket market;
    public static final Logger log = Logger.getLogger("Market");

    public static void main(String[] args) throws Exception {
        log.info("----Market is starting-----");

        try {
            socket = new Socket("localhost", 5051);
        } catch (Exception e){
            log.severe(String.format("Market cannot start, router might be unavailable [%s]", e.getMessage()));
            socket.close();
            System.exit(1);
        }

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

//        while (true){
//            try {
//                log.info("Waiting for request");
//
//
//            }
//        }
    }

    private static void receiveAndSetLogonId() throws IOException {
        Message initMsg = new Message(in.readLine());
        market.setId(initMsg.get("553"));
        log.info(String.format("Market [%s] added to routing table", market.getId()));
    }

}