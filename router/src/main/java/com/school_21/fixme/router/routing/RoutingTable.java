package com.school_21.fixme.router.routing;

import com.school_21.fixme.router.Router;

import java.net.Socket;
import java.util.ArrayList;

public class RoutingTable {
    private final ArrayList<RouteEntry> routeEntries = new ArrayList<>();
    private int brokerCount = 0;
    private int marketCount = 0;

    public RoutingTable(){}

    public void addEntry(RouteEntry entry){
        entry.setId(generateId(entry.getType()));
        this.routeEntries.add(entry);
    }

    private String generateId(String type){
        if (type.equals("broker")){
            return String.format("B%06d", ++brokerCount);
        }
        return String.format("M%06d", ++marketCount);
    }

    public RouteEntry findEntry(String clientId){
        return routeEntries.stream().filter(it ->
                it.getId().equalsIgnoreCase(clientId)).findFirst().orElse(null);
    }

    public RouteEntry findMarket(String name) {
        return routeEntries.stream().filter(it ->
            it.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    public void deleteEntry(Socket socket){
        routeEntries.removeIf(it -> it.getSocket() == socket);
    }
}
