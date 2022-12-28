package com.school_21.fixme.router.process;

import com.school_21.fixme.router.request.Request;
import com.school_21.fixme.router.request.RequestType;
import com.school_21.fixme.router.response.Response;
import com.school_21.fixme.utils.FixProtocol;

public class ClassificationProcessor extends RequestHandler{

    public ClassificationProcessor(RequestHandler handler){
        super(handler);
    }

    @Override
    public Response process(Request request){
        String msgType = request.getMessage().getRawMessage();
//        var parts = msgType.split("\\|");
        switch (msgType){
            case "1": request.setRequestType(RequestType.BUY);
            case "2": request.setRequestType(RequestType.SELL);
            default: return new Response(request.getSocket(), FixProtocol.failResponse(request.getSocket().getInetAddress().toString()));
        }
    }
}
