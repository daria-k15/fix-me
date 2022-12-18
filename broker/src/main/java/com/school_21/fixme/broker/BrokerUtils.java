package com.school_21.fixme.broker;

public class BrokerUtils {
    public static void assignRouteServiceId(String value){
        String[] parts = value.split("-");
        BrokerAccount.brokerRouteId = Integer.parseInt(parts[0]);
        BrokerAccount.brokerServiceId = Integer.parseInt(parts[1]);
    }
}
