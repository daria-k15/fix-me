package com.school_21.fixme.router;


import com.school_21.fixme.router.database.Database;
import com.school_21.fixme.router.routing.RoutingTable;
import com.school_21.fixme.router.sockets.SocketServer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Router {
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format","[%1$tF %1$tT] [\u001b[32;1mROUTER\u001b[0m] [%4$-7s] %5$s %n");
    }
    public static final RoutingTable routingTable = new RoutingTable();
    public static final ExecutorService executor = Executors.newFixedThreadPool(100);
    public static final Logger log = Logger.getLogger("Router");

    public static void main(String[] args) {
        log.info("-----Router is starting-----");

        try {
            Database.createTransactionSchema();
        } catch (Exception e){
            System.out.println("Smth wrong with database! " + e.getMessage());
        }

        executor.submit(new SocketServer(5000, "broker"));
        executor.submit(new SocketServer(5001, "market"));
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e){
            log.severe("Interrupted request: " + e.getMessage());
            System.exit(1);
        }
    }
}