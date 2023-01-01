package com.school_21.fixme.router.process;

import com.school_21.fixme.router.Router;
import com.school_21.fixme.router.request.Request;
import com.school_21.fixme.router.request.RequestType;
import com.school_21.fixme.router.response.Response;
import com.school_21.fixme.router.routing.RouteEntry;
import com.school_21.fixme.utils.FixProtocol;

import java.net.Socket;

public class RelayProcessor extends RequestHandler{
    public RelayProcessor(RequestHandler requestHandler){
        super(requestHandler);
    }

    @Override
    public Response process(Request request) {
        if (request.getRequestType() == RequestType.BUY || request.getRequestType() == RequestType.SELL){
            String marketName = request.getMessage().get("103");
            RouteEntry entry = Router.routingTable.findMarket(marketName);

            if (entry != null) {
                return new Response(entry.getSocket(), request.getMessage());
            } else {
                return new Response(request.getSocket(), FixProtocol.failResponse(request.getSocket().toString()));
            }
        }
        return new Response(request.getSocket(), FixProtocol.failResponse(request.getSocket().toString()));
    }
}
