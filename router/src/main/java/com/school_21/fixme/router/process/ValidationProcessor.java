package com.school_21.fixme.router.process;

import com.school_21.fixme.router.request.Request;
import com.school_21.fixme.router.response.Response;
import com.school_21.fixme.utils.FixProtocol;
import lombok.extern.slf4j.Slf4j;

import java.util.logging.Logger;

public class ValidationProcessor extends RequestHandler{
    private static final Logger log = Logger.getLogger( "Router" );

    public ValidationProcessor(RequestHandler handler){
        super(handler);
    }

    @Override
    public Response process(Request request) {
        log.info(String.format("Received message [%s] from %s => ", request.getMessage(), request.socketName()));
        try {
            FixProtocol.validateMessage(request.getMessage());
            return next.process(request);
        } catch (Exception e) {
            log.warning("Message is invalid");
            return new Response(request.getSocket(), FixProtocol.failResponse(request.getMessage().get("553"), "Message is invalid"));
        }
    }
}
