package com.school_21.fixme.router.process;

import com.school_21.fixme.router.Router;
import com.school_21.fixme.router.request.Request;
import com.school_21.fixme.router.response.Response;
import com.school_21.fixme.router.routing.RouteEntry;
import com.school_21.fixme.router.routing.RoutingTable;
import com.school_21.fixme.utils.FixProtocol;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MarketIdentifyProcessor extends RequestHandler{
    RoutingTable routingTable;

    public MarketIdentifyProcessor(RequestHandler nextHandler) {
        super(nextHandler);
    }
    @Override
    public Response process(Request request){
        String id = request.getMessage().get("553");
        String name = request.getMessage().get("103");

        if (id.length() > 0 && name.length() > 0){
            RouteEntry market = Router.routingTable.findEntry(id);
            market.setName(name);
            log.info("Market name {} added to {} in routing table", name, id);
            return null;
        }
        return new Response(request.getSocket(), FixProtocol.failResponse(request.getMessage().get("553")));
    }
}
