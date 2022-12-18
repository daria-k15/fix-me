package com.school_21.fixme.router.routing;

import java.net.Socket;
import java.util.ArrayList;

//@Component
public class RoutingTable {
    private final ArrayList<RouteEntry> routeEntries = new ArrayList<>();
    private Integer brokerCount = 0;
    private Integer marketCount = 0;

    public void addEntry(RouteEntry entry){
        entry.setId(generateId(entry.getType()));
        this.routeEntries.add(entry);
    }

    private String generateId(String type){
        if (type.equals("broker")){
            return String.format("B%010d", ++brokerCount);
        }
        return String.format("M%10d", ++marketCount);
    }

    public RouteEntry findEntry(String clientId){
        return routeEntries.stream().filter(it ->
                it.getId().equalsIgnoreCase(clientId)).findFirst().orElse(null);
    }

    public RouteEntry findMarket(String name) {
        return routeEntries.stream().filter(it ->
            it.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void deleteEntry(Socket socket){
        routeEntries.removeIf(it -> it.getSocket() == socket);
    }
}
