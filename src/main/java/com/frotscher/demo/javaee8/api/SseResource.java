package com.frotscher.demo.javaee8.api;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Singleton
@Path("sse")
public class SseResource {

    @Context
    private Sse sseContext;

    private Map<Long, SseEventSink> eventSinks = new ConcurrentHashMap<>();
    private AtomicLong sseIds = new AtomicLong();

    private Logger log = Logger.getLogger(SseResource.class.getName());

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void subscribe(@Context SseEventSink eventSink) {
        log.info("Received listener subscription for SSE.");
        Long sseId = sseIds.incrementAndGet();
        eventSinks.put(sseId, eventSink);
        eventSink.send(sseContext.newEvent("Welcome! Your subscriber ID is: " + sseId));
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void broadcast(@FormParam("event") String event) {
        log.info("Broadcasting new event...");

        for (Map.Entry<Long, SseEventSink> entry : eventSinks.entrySet()) {
            SseEventSink sink = entry.getValue();
            if (sink.isClosed()) {
                log.info("Event sink in closed!!!");

            } else {
                OutboundSseEvent sseEvent = sseContext.newEvent(event);
                log.info("Sending registration event to subscriber " + entry.getKey());
                sink.send(sseEvent);
            }
        }
        log.info("Done.");
    }

    /*
    public void broadcastRegistrationEvent(@ObservesAsync RegistrationEvent registrationEvent) {
        log.info("Processing registration event...");
        log.info("SSE Context: " + sseContext);
        for (Map.Entry<Long, SseEventSink> entry : eventSinks.entrySet()) {
            SseEventSink sink = entry.getValue();
            if (sink.isClosed()) {
                log.info("Event sink in closed!!!");

            } else {
                OutboundSseEvent sseEvent = sseContext.newEvent(registrationEvent.getName());
                log.info("Sending registration event to subscriber " + entry.getKey());
                sink.send(sseEvent);
            }
        }
        log.info("Done.");
    }
    */
}
