package com.school_21.fixme.router.routing;

import java.net.Socket;

public class MarketRouteEntry extends RouteEntry {
    public MarketRouteEntry(Socket socket) {
        super(socket, "market");
        this.setId("553");
    }
}
