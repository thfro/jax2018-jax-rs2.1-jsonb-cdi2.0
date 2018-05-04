package com.frotscher.demo.javaee8.api;

import com.frotscher.demo.javaee8.events.RegistrationEvent;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;
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
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Singleton
@Path("sse/broadcast")
public class SseBroadcasterResource {

    @Context
    private Sse sseContext;

    private SseBroadcaster sseBroadcaster;
    private AtomicLong sseIds = new AtomicLong();
    private Logger log = Logger.getLogger(SseBroadcasterResource.class.getName());

    @PostConstruct
    public void init() {
        log.info("Initializing SSE broadcaster...");
        this.sseBroadcaster = sseContext.newBroadcaster();
    }


    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void subscribe(@Context SseEventSink eventSink) {
        log.info("Received listener subscription for SSE.");
        Long sseId = sseIds.incrementAndGet();
        sseBroadcaster.register(eventSink);
        eventSink.send(sseContext.newEvent("Welcome! Your subscription ID is : " + sseId));
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void broadcast(@FormParam("event") String event) {
        sseBroadcaster.broadcast(sseContext.newEvent(event));
    }


    /* This should actually work, but causes a NPE in the current Liberty beta.

    public void broadcastRegistrationEvent(@ObservesAsync RegistrationEvent registrationEvent) {
        log.info("Processing registration event...");
        Long sseId = System.currentTimeMillis();
        OutboundSseEvent sseEvent = sseContext.newEvent(sseId + ": New registration: " + registrationEvent.getName());
        sseBroadcaster.broadcast(sseEvent);
        log.info("Done.");
    }
    */
}
