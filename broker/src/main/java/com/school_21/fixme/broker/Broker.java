package com.school_21.fixme.broker;

import com.school_21.fixme.market.markets.CryptoMarket;
import com.school_21.fixme.market.markets.Instrument;
import com.school_21.fixme.market.markets.Market;
import com.school_21.fixme.utils.FixProtocol;
import com.school_21.fixme.utils.messages.Message;
import com.school_21.fixme.utils.orders.OrderType;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

@Slf4j
public class Broker {
    private static final Scanner scanner = new Scanner(System.in);
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;

    private static String id;

    public static void main(String[] args) throws Exception {
        log.info("--------- Broker is starting---------");

        try {
            socket = new Socket("localhost", 5000);
        } catch (Exception e) {
            log.error(String.format("Broker couldn't start, router might be unavailable: %s", e.getMessage()));
            System.exit(1);
        }
        out = new PrintWriter(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        //Logon msg
        String serverResponse = in.readLine();
        log.info("Received: {}", serverResponse);

        receivedMessage(serverResponse);
        createAndHandleOrder();
    }

    private static void createAndHandleOrder() {
        System.out.println("\nWelcome to crypto market. May the odds be ever in your favor!");
        System.out.println("Press any key to continue");
        scanner.nextLine();

        while (true) {
            OrderType orderType = selectOrderType();
            String instrument = selectInstrument(new CryptoMarket()).getName();
            Integer amount = getAmount(orderType);
            Double price = getPrice(orderType);

            Message fixMessage = FixProtocol.orderMessage(Broker.id, instrument, amount.toString(), price.toString(), Integer.toString(BrokerAccount.brokerRouteId), orderType);
            out.println(fixMessage);
            out.flush();

            handleRouterReply();
        }
    }

    private static OrderType selectOrderType() {
        cls();

        System.out.println("Would you like to buy or sell?");
        while (true) {
            System.out.println("1 - buy");
            System.out.println("2 - sell");
            String answer = scanner.nextLine();
            if (answer.equals("1")) {
                return OrderType.BUY;
            } else if (answer.equals("2")) {
                return OrderType.SELL;
            } else {
                System.out.println("Unknown type. Try again, please");
            }
        }
    }

    private static Instrument selectInstrument(Market market) {
        cls();

        int count = 1;
        for (Instrument instrument : market.getInstruments()) {
            System.out.println(String.format("% 2d) %-7s %s", count, instrument.getName(), instrument.getCode()));
            ++count;
        }

        while (true) {
            System.out.print("\nSelect an instrument form 1 to 6: ");
            int answer = scanner.nextInt();
            if (answer > 0 && answer < 7) {
                return market.getInstruments().get(answer - 1);
            } else {
                System.out.println("Invalid number. Try again!");
            }
        }
    }

    private static Double getPrice(OrderType type) {
        cls();

        String order;
        if (type.equals(OrderType.BUY)) {
            order = "How much would you like to spend for each unit?: ";
        } else {
            order = "How much would you like to sell each unit for?: ";
        }
        while (true) {
            try {
                System.out.print(order);
                return Double.parseDouble(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Unit must be a number!");
            }
        }
    }

    private static Integer getAmount(OrderType type) {
        cls();

        String order;
        if (type.equals(OrderType.BUY)) {
            order = "How many units would you like to buy: ";
        } else {
            order = "How many units would you like to sell: ";
        }
        while (true) {
            System.out.print(order);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Must be an integer!");
            }
        }
    }

    private static void cls() {
        if (!System.getProperty("os.name").startsWith("Win")) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }

    private static void receivedMessage(String logonMsg) {
        Message msg = new Message(logonMsg);
        if (msg.get("35").equals("L")) {
            Broker.id = msg.get("553");
            log.info("Broker added to routingTable! BrokerId: {}", BrokerAccount.brokerRouteId);
        }
    }

    private static void handleRouterReply() {
        Message message = null;
        try {
            message = new Message(in.readLine());
        } catch (Exception e) {
            log.error("Connection to router lost");
            System.exit(1);
        }
        String msgType = message.get("35");
        switch (msgType) {
            case "4":
                System.out.println("Order was successfully completed");
                break;
            case "3":
                System.out.println("Order was rejected");
                break;
            case "E":
                System.out.println("An error has occured");
                break;
            default:
                System.out.println("Got an unknown response: " + message.toString());
                break;
        }
        System.out.println("Press any key to start new order");

        scanner.nextLine();
    }
}