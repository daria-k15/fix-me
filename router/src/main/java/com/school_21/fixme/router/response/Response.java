package com.school_21.fixme.router.response;

import com.school_21.fixme.utils.messages.Message;
import lombok.Data;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

@Data
public class Response {
    private Socket destination;
    private Message message;

    public Response (Socket destination, Message message){
        this.destination = destination;
        this.message = message;
    }

    public void send() throws IOException{
        PrintWriter out = new PrintWriter(this.destination.getOutputStream(), true);
        out.println(this.message);
    }
}
