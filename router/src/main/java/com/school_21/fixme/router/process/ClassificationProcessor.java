package com.school_21.fixme.router.process;

import com.school_21.fixme.router.Router;
import com.school_21.fixme.router.request.Request;
import com.school_21.fixme.router.request.RequestType;
import com.school_21.fixme.router.response.Response;
import com.school_21.fixme.utils.FixProtocol;

public class ClassificationProcessor extends RequestHandler {

    public ClassificationProcessor(RequestHandler handler) {
        super(handler);
    }

    @Override
    public Response process(Request request) {
        String msgType = request.getMessage().get("35");
        if (msgType.equals("1")) {
            Router.log.info("Message classified as BUY");
            request.setRequestType(RequestType.BUY);
        } else if (msgType.equals("2")) {
            Router.log.info("Message classified as SELL");
            request.setRequestType(RequestType.SELL);
        } else if (msgType.equals("I")) {
            Router.log.info("Message classified as IDENTIFY");
            request.setRequestType(RequestType.IDENTIFY);
            return new MarketIdentifyProcessor(null).process(request);
        } else {
            return new Response(request.getSocket(), FixProtocol.failResponse(request.getSocket().getInetAddress().toString()));
        }
        return next.process(request);
    }
}
