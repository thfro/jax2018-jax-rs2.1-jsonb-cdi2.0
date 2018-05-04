package com.frotscher.demo.javaee8.events;

public class RegistrationEvent {
    private String name;

    public RegistrationEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
