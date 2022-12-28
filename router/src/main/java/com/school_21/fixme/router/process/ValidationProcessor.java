package com.school_21.fixme.router.process;

import com.school_21.fixme.router.request.Request;
import com.school_21.fixme.router.response.Response;
import com.school_21.fixme.utils.FixProtocol;

import java.util.logging.Logger;

public class ValidationProcessor extends RequestHandler{
    public ValidationProcessor(RequestHandler handler){
        super(handler);
    }

    @Override
    public Response process(Request request) {
        Logger log = Logger.getLogger("Router");
        log.info(String.format("Received message [%s] from %s => ", request.getMessage(), request.socketName()));
        try {
            FixProtocol.validateMessage(request.getMessage());
            return next.process(request);
        } catch (Exception e) {
            log.warning("Message is invalid");
            return new Response(request.getSocket(), FixProtocol.failResponse(request.getSocket().getInetAddress().toString()));
        }
    }
}
