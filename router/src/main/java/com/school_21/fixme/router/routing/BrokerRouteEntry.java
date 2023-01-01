package com.school_21.fixme.router.routing;

import java.net.Socket;

public class BrokerRouteEntry extends RouteEntry {
    public BrokerRouteEntry(Socket socket) {
        super(socket, "broker");
        this.setId("553");
    }
}
