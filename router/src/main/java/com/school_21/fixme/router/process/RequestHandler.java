package com.school_21.fixme.router.process;

import com.school_21.fixme.router.request.Request;
import com.school_21.fixme.router.response.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public abstract class RequestHandler {
    private final RequestHandler next;

    public void handleRequest(Request request){
        if (next != null){
            next.handleRequest(request);
        }
    }

    protected void printHandling(Request request) {
        log.info("{} handling request {}", this, request);
    }

    protected abstract Response process(Request request);
}
