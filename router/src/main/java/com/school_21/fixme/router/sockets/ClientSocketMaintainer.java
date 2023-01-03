package com.school_21.fixme.router.sockets;

import com.school_21.fixme.router.Router;
import com.school_21.fixme.router.process.ClassificationProcessor;
import com.school_21.fixme.router.process.RelayProcessor;
import com.school_21.fixme.router.process.RequestHandler;
import com.school_21.fixme.router.process.ValidationProcessor;
import com.school_21.fixme.router.request.Request;
import com.school_21.fixme.router.response.Response;
import com.school_21.fixme.utils.messages.Message;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

@Slf4j
public class ClientSocketMaintainer implements Runnable {
    private Socket socket;
    private String socketName;

    public ClientSocketMaintainer(Socket socket) {
        this.socket = socket;
        String ip = socket.getInetAddress().toString();
        int port = socket.getPort();
        this.socketName = String.format("%s:%d", ip, port);
    }

    public void run() {
        log.info(String.format("Starting socket maintainer for %s", socketName));

        RequestHandler handler = new ValidationProcessor(new ClassificationProcessor(
                new RelayProcessor(null)
        ));

        String inLine;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while ((inLine = in.readLine()) != null) {
                Response response = handler.process(new Request(socket, new Message(inLine)));
                if (response != null) {
                    response.send();
                }
            }
        } catch (IOException e) {
            log.error(String.format("Got error while processing message from %s %s", socketName, e.getMessage()));
        }
        // Delete RouteEntry when this thread is done processing
        log.info(String.format("Socket maintainer for %s shutting down", socketName));
        Router.routingTable.deleteEntry(this.socket);
    }
}
