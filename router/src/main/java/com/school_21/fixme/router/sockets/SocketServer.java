package com.school_21.fixme.router.sockets;

import com.school_21.fixme.router.Router;
import com.school_21.fixme.router.routing.BrokerRouteEntry;
import com.school_21.fixme.router.routing.MarketRouteEntry;
import com.school_21.fixme.router.routing.RouteEntry;
import com.school_21.fixme.utils.FixProtocol;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class SocketServer implements Runnable {
    private Integer port;
    private Integer backlog;
    private String type;
    private static final Logger log = Logger.getLogger( "Router" );

    public SocketServer(Integer port, String type) {
        this.port = port;
        this.type = type;
        this.backlog = 1000;
    }

    public void run() {
        ServerSocket socket;

        log.info(String.format("Starting %s SocketServer thread on port %d", this.type, this.port));
        try {
            // Instantiate a socket server on 0.0.0.0:500X for new broker/market socket connections
            socket = new ServerSocket(this.port, this.backlog, Inet4Address.getByName("0.0.0.0"));
            while (true) {
                Socket clientSocket = socket.accept();

                String ip = clientSocket.getInetAddress().toString();
                log.info(String.format("Got connection from %s:%d", ip, clientSocket.getPort()));
                RouteEntry entry;
                if (this.type.equalsIgnoreCase("broker")) {
                    entry = new BrokerRouteEntry(clientSocket);
                } else {
                    entry = new MarketRouteEntry(clientSocket);
                }
                Router.routingTable.addEntry(entry);

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println(FixProtocol.logonMessage(entry.getId()));

                Router.executor.submit(new ClientSocketMaintainer(clientSocket));
            }
        } catch (Exception e) {
            log.severe("Couldn't start Broker Server: " + e.getMessage());
        }
    }
}
