package com.school_21.fixme.router.process;

import com.school_21.fixme.router.request.Request;
import com.school_21.fixme.router.request.RequestType;
import com.school_21.fixme.router.response.Response;
import com.school_21.fixme.utils.FixProtocol;
import java.util.logging.Logger;

public class ClassificationProcessor extends RequestHandler {
    private static final Logger log = Logger.getLogger( "Router" );

    public ClassificationProcessor(RequestHandler handler) {
        super(handler);
    }

    @Override
    public Response process(Request request) {
        String msgType = request.getMessage().get("35");
        if (msgType.equals("1")) {
            log.info("Message classified as [BUY]");
            request.setRequestType(RequestType.BUY);
        } else if (msgType.equals("2")) {
            log.info("Message classified as [SELL]");
            request.setRequestType(RequestType.SELL);
        } else if (msgType.equals("I")) {
            log.info("Message classified as [IDENTIFY]");
            request.setRequestType(RequestType.IDENTIFY);
            return new MarketIdentifyProcessor(null).process(request);
        } else if (msgType.equals("3")) {
            log.info("Message classified as [REJECT]");
            request.setRequestType(RequestType.REJECT);
        } else if (msgType.equals("4")) {
            log.info("Message classified as [EXECUTE]");
            request.setRequestType(RequestType.EXECUTE);
        } else {
            return new Response(request.getSocket(), FixProtocol.failResponse(request.getMessage().get("553"), "Unknown message type"));
        }
        return next.process(request);
    }
}
