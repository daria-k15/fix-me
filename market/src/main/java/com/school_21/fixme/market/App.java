package com.school_21.fixme.market;

import com.school_21.fixme.market.markets.CryptoMarket;
import com.school_21.fixme.market.markets.Instrument;
import com.school_21.fixme.utils.FixProtocol;
import com.school_21.fixme.utils.messages.Message;
import com.school_21.fixme.utils.orders.Orders;
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
    private static CryptoMarket market = new CryptoMarket();
    public static final Logger log = Logger.getLogger("Market");

    public static void main(String[] args) throws Exception {
        log.info("----Market is starting-----");

        try {
            socket = new Socket("127.0.0.1", 5001);
        } catch (Exception e){
            log.severe(String.format("Market cannot start, router might be unavailable [%s]", e.getMessage()));
            socket.close();
            System.exit(1);
        }

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        receiveAndSetLogonId();
        identifyMarket();
        while (true){
            try {
                log.info("Waiting for request");
                String inputLine = in.readLine();

                if (inputLine == null){
                    log.severe("Connection to router lost");
                    break;
                }
                log.info(String.format("Received message: %s", inputLine));
                Message msg = validateInitialMessage(new Message(inputLine));

                if (!msg.isValid()){
                    log.severe(String.format("Sending :: Rejection :: %s", msg.toString()));
                    out.println(msg.toString());
                    break;
                }

                Orders order = new Orders(msg);
                switch (msg.get("35")){
                    case "1":
                        log.info("processing buy order");
                        if (market.instrumentByCode(msg.get("I")) == null) {
                            throw new Exception("Cannot found instrument");
                        }

                }
            } catch (Exception e){

            }
        }
    }

    private static void receiveAndSetLogonId() throws IOException {
        Message initMsg = new Message(in.readLine());
        market.setId(initMsg.get("553"));
        log.info(String.format("Market [%s] added to routing table", market.getId()));
    }

    private static void identifyMarket() {
        Message name = FixProtocol.identifyMessage(App.market.getId(), App.market.getName());
        out.println(name);
        log.info(String.format("Sent market name [%s] to router", App.market.getName()));
    }

    private static Message validateInitialMessage(Message msg) {
        String market = msg.get("103");
        String instrument = msg.get("100");
        String amount = msg.get("101");
        String price = msg.get("102");
        String clientId = msg.get("109");

        if (market.isEmpty()) return FixProtocol.failResponse("Market hasn't been specified");
        if (instrument.isEmpty()) return FixProtocol.failResponse("Instrument hasn't been specified");
        if (amount.isEmpty()) return FixProtocol.failResponse("Amount hasn't been specified");
        if (price.isEmpty()) return FixProtocol.failResponse("Price hasn't been specified");
        if (clientId.isEmpty()) return FixProtocol.failResponse("ClientId hasn't been specified");
        return msg;
    }

    private static void tryBuyOrder(Orders order) {
        Instrument instrument = market.instrumentByCode(order.getInstrument());

        if (!order.isValid()){

        }
    }
}