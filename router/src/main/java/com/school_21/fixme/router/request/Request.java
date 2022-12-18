package com.school_21.fixme.router.request;

import com.school_21.fixme.utils.messages.Message;
import lombok.Data;

import java.net.Socket;

@Data
public class Request {
    private Socket socket;
    private RequestType requestType;
    private Message message;
    private boolean handled;

    public Request(Socket socket, Message message){
        this.socket = socket;
        this.message = message;
    }

    public boolean markedHandled() {
        return this.handled = true;
    }

    public boolean isHandled() {
        return this.handled;
    }

}
