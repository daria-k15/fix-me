package com.school_21.fixme.router;

import java.net.Socket;

public class Counter {
    public static int brokerCounter = 0;
    public static int marketCount = 0;
    public static int serviceId = 100000;

    public static String getBrokerRouteID(Socket s) {
        return Integer.toString(brokerCounter);
    }

    public static int countBroker(){
        return brokerCounter++;
    }

    public static int generateServiceId(){
        return serviceId++;
    }
}
