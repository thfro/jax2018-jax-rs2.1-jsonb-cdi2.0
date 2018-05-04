package com.frotscher.demo.javaee8.services;

import com.frotscher.demo.javaee8.api.dto.RegistrationDetails;
import com.frotscher.demo.javaee8.events.RegistrationEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@ApplicationScoped
public class RegistrationService {

    private Logger log = Logger.getLogger(RegistrationService.class.getName());

    private Map<Long, RegistrationDetails> registrations = new ConcurrentHashMap<>();
    private AtomicLong registrationIds = new AtomicLong();

    @Inject
    private Event<RegistrationEvent> registrationEvent;


    public Collection<RegistrationDetails> getRegistrations() {
        return registrations.values();
    }

    public RegistrationDetails getRegistration(Long id) {
        return registrations.get(id);
    }

    public Long createRegistration(RegistrationDetails registrationDetails) {
        Long registrationId = registrationIds.incrementAndGet();
        registrationDetails.setId(registrationId);
        registrationDetails.setCreated(ZonedDateTime.now());

        registrations.put(registrationId, registrationDetails);

        log.info("Processed new registration: " + registrationDetails.getName());

        // synchronous CDI event
        //registrationEvent.fire(new RegistrationEvent(registrationDetails.getName()));

        // synchronous CDI event - note the use of CompletionStage
        registrationEvent
          .fireAsync(new RegistrationEvent(registrationDetails.getName()))
          .exceptionally(t -> { log.severe(t.getMessage()); t.printStackTrace(); return null; });

        return registrationId;
    }

    public void updateRegistration(Long id, RegistrationDetails details) {
        registrations.put(id, details);
    }
}
