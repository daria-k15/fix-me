package com.school_21.fixme.router;


import com.school_21.fixme.router.routing.RoutingTable;
import com.school_21.fixme.router.sockets.SocketServer;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Router {
    public static final RoutingTable routingTable = new RoutingTable();
    public static final ExecutorService executor = Executors.newFixedThreadPool(100);

    public static void main(String[] args) {
        log.info("-----Router is starting-----");

        executor.submit(new SocketServer(5000, "broker"));
        executor.submit(new SocketServer(5001, "market"));
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e){
            log.error("Interrupted request: " + e.getMessage());
            System.exit(1);
        }
    }
}