package com.school_21.fixme.broker;

import com.school_21.fixme.market.markets.CryptoMarket;
import com.school_21.fixme.market.markets.Instrument;
import com.school_21.fixme.market.markets.Market;
import com.school_21.fixme.utils.FixProtocol;
import com.school_21.fixme.utils.orders.OrderType;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Logger;

public class Broker {
    public static FixProtocol fixProtocol;
    public static BrokerThread thread;

    private static BrokerUtils utils;
    private static Boolean exitCase;
    private static Scanner scanner = new Scanner(System.in);
    private static PrintWriter out;
    private static BufferedReader in;

    private static final Logger log = Logger.getLogger("Broker");
    public static void main(String[] args) {
        log.info("--------- Broker is starting---------\n");

//        try{
//            Socket socket = new Socket("localhost", 5000);
//            out = new PrintWriter(socket.getOutputStream(), true);
//            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            String serverResponse = in.readLine();
//            utils.assignRouteServiceId(serverResponse);
//            log.info(String.format("Broker connected! BrokerId: %s, ServiceId: %s", BrokerAccount.brokerRouteId, BrokerAccount.brokerServiceId));
//        } catch (Exception e){
//            log.severe(String.format("Broker couldn't start, router might be unavailable: %s", e.getMessage()));
//            System.exit(1);
//        }

        fixProtocol = new FixProtocol(Integer.toString(BrokerAccount.brokerRouteId));

        while (true){
            String fixMessage;
            OrderType orderType = selectOrderType();
            String instrument = selectInstrument(new CryptoMarket()).getName();
            Integer amount = getAmount();
            Double price = getPrice(orderType);

            if (orderType.equals(OrderType.BUY)){
                fixMessage = fixProtocol.purchaseMessage(instrument, amount.toString(), price.toString(), Integer.toString(BrokerAccount.brokerRouteId));
                System.out.println(fixMessage);
            } else if (orderType.equals(OrderType.SELL)) {
                fixMessage = fixProtocol.saleMessage(instrument, amount.toString(), price.toString(), Integer.toString(BrokerAccount.brokerRouteId));
                System.out.println(fixMessage);
            }
        }
    }

    private static OrderType selectOrderType() {
        System.out.println("Would you like to buy or sell?");
        while (true) {
            System.out.println("1 - buy");
            System.out.println("2 - sell");
            String answer = scanner.nextLine();
            if (answer.equals("1")){
                return OrderType.BUY;
            } else if (answer.equals("2")){
                return OrderType.SELL;
            }
            System.out.println("Unknown type. Try again, please");
        }
    }

    private static Instrument selectInstrument(Market market) {
        int count = 1;
        for (Instrument instrument : market.getInstruments()){
            System.out.println(String.format("% 2d) %-7s %s", count, instrument.getName(), instrument.getCode()));
            ++count;
        }

        while (true) {
            try {
                System.out.println("\nSelect an instrument form 1 to 6");
                int answer = scanner.nextInt();
                if (answer > 0 && answer < 7) {
                    return market.getInstruments().get(answer - 1);
                }
            } catch (Exception e){
                System.out.println("Invalid number. Try again!");
            }
        }
    }

    private static Double getPrice(OrderType type){
        String order;
        if (type.equals(OrderType.BUY)){
            order = "How mush would you like to spend for each unit? : ";
        } else {
            order = "How much would you like to sell each unit for? : ";
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

    private static Integer getAmount() {
        while (true) {
            try {
                System.out.println("How many units would you like to buy or sell?");
                return Integer.parseInt(scanner.nextLine());
            } catch (Exception e){
                System.out.println("Must be an integer!");
            }
        }
    }
}