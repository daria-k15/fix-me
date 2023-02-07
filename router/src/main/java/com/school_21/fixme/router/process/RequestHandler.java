package com.school_21.fixme.router.process;

import com.school_21.fixme.router.request.Request;
import com.school_21.fixme.router.response.Response;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class RequestHandler {
    protected final RequestHandler next;

    public void handleRequest(Request request) {
        if (next != null) {
            next.handleRequest(request);
        }
    }

    public abstract Response process(Request request);
}
