package com.frotscher.demo.javaee8.services;

import com.frotscher.demo.javaee8.events.RegistrationEvent;

import javax.enterprise.event.Observes;
import javax.enterprise.event.ObservesAsync;
import java.util.logging.Logger;

public class EmailService {

    private Logger log = Logger.getLogger(EmailService.class.getName());

    public void emailRegistrationConfirmationSync(@Observes RegistrationEvent event) {
        sendEmail();
        log.info("Done with sending confirmation synchronously");
    }

    public void emailRegistrationConfirmationAsync(@ObservesAsync RegistrationEvent event) {
        sendEmail();
        log.info("Done with sending confirmation asynchronously");
    }

    private void sendEmail() {
        log.info("Sending confirmations handled by thread " + Thread.currentThread().getName());

        // Simulate that we are busy sending emails for some time. This is only to demonstrate
        // the positive effect of asynchronous events to response time. If CDI events are fired
        // and processed synchronously the client receives a HTTP response only after 5 seconds.
        // When using asynchronous events, the response is received immediately, as time-consuming
        // tasks are handled in a separate thread on the server.
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}