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

public class Market {
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [\u001b[35;1mMARKET\u001b[0m] [%4$-7s] %5$s %n");
    }
    public static final Logger log = Logger.getLogger( "Market" );
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;
    private static CryptoMarket market = new CryptoMarket();

    public static void main(String[] args) throws Exception {
        log.info("----Market is starting-----");

        try {
            socket = new Socket("localhost", 5001);
        } catch (Exception e){
            log.severe("Market cannot start, router might be unavailable " + e.getMessage());
            socket.close();
            System.exit(1);
        }

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        receiveAndSetLogonId();
        identifyMarket();
        printInstrument();
        receiveAndValidateMessage();
    }

    private static void receiveAndValidateMessage() {
        while (true){
            Orders order = null;
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

                order = new Orders(msg);
                switch (msg.get("35")){
                    case "1":
                        log.info("Processing buy order");
                        if (market.instrumentByCode(msg.get("100")) == null) {
                            throw new Exception("Cannot found instrument");
                        }
                        tryBuyOrder(order);
                        break;
                    case "2":
                        log.info("Processing sell order");
                        if (market.instrumentByCode(msg.get("100")) == null) {
                            throw new Exception("Cannot found instrument");
                        }
                        trySellOrder(order);
                        break;
                    default:
                        throw new Exception("Unknown order type");
                }
            } catch (Exception e){
                log.severe("Rejection: " + e.getMessage());
                FixProtocol.rejectOrder(order);
            }
        }
    }
    private static void receiveAndSetLogonId() throws IOException {
        Message initMsg = new Message(in.readLine());
        market.setId(initMsg.get("553"));
        log.info(String.format("Market [%s] added to routing table", market.getId()));
    }

    private static void printInstrument() {
        for (Instrument i : market.getInstruments()) {
            log.info(String.format("%-20s[%-5s] => Units = %-4d : minBuy=%-20.2f : maxSell=%-20.2f", i.getName(),
                    i.getCode(), i.getAvailableAmount(), i.getMinBuyPrice(), i.getMaxSellPrice()));
        }
    }

    private static void identifyMarket() {
        Message name = FixProtocol.identifyMessage(Market.market.getId());
        out.println(name);
        log.info(String.format("Sent market name [%s] to router", Market.market.getName()));
    }

    private static Message validateInitialMessage(Message msg) {
        String market = msg.get("103");
        String instrument = msg.get("100");
        String amount = msg.get("101");
        String price = msg.get("102");
        String clientId = msg.get("553");

        if (market.isEmpty()) return FixProtocol.failResponse("Market hasn't been specified");
        if (instrument.isEmpty()) return FixProtocol.failResponse("Instrument hasn't been specified");
        if (amount.isEmpty()) return FixProtocol.failResponse("Amount hasn't been specified");
        if (price.isEmpty()) return FixProtocol.failResponse("Price hasn't been specified");
        if (clientId.isEmpty()) return FixProtocol.failResponse("ClientId hasn't been specified");
        return msg;
    }

    private static void tryBuyOrder(Orders order) {
        Instrument instrument = market.instrumentByCode(order.getInstrument());

        int requestedAmount = Integer.parseInt(order.getQuantity());
        double requestedPrice = Double.parseDouble(order.getPrice());

        if (!order.isValid()){
            sendMessage(FixProtocol.rejectOrder(order));
            return;
        }
        if (requestedPrice >= instrument.getMinBuyPrice()){
            if (requestedAmount <= instrument.getAvailableAmount()){
                log.info("Message accepted");
                instrument.setAvailableAmount(instrument.getAvailableAmount() - requestedAmount);
                sendMessage(FixProtocol.acceptOrder(order));
            } else {
                log.severe(String.format("Rejected : Not enough %s units to complete order", instrument.getName()));
                sendMessage(FixProtocol.rejectOrder(order));
            }
        } else {
            log.severe(String.format("Rejected : Buy price for %s is too low", instrument.getName()));
            sendMessage(FixProtocol.rejectOrder(order));
        }
    }

    private static void trySellOrder(Orders order){
        Instrument instrument = market.instrumentByCode(order.getInstrument());
        int requestedSellAmount = Integer.parseInt(order.getQuantity());
        double requestedSellPrice = Double.parseDouble(order.getPrice());

        if (requestedSellPrice <= instrument.getMaxSellPrice()){
            instrument.setAvailableAmount(instrument.getAvailableAmount() + requestedSellAmount);
            sendMessage(FixProtocol.acceptOrder(order));
        } else {
            log.severe(String.format("Rejected: Sell price for %s is too high", instrument.getName()));
            sendMessage(FixProtocol.rejectOrder(order));
        }
    }

    public static void sendMessage(Message message) {
        out.println(message.toString());
    }
}