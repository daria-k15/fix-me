package com.school_21.fixme.router.routing;

import lombok.Data;

import java.net.Socket;

@Data
public class RouteEntry {
    private Socket socket;
    private String type;
    private String idTag;
    private String id;

    private String name = "";

    public RouteEntry(Socket socket, String type){
        this.socket = socket;
        this.type = type;
    }
}
