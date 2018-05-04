package com.frotscher.demo.javaee8.api.dto;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbPropertyOrder;
import javax.json.bind.annotation.JsonbTransient;
import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.json.bind.config.PropertyOrderStrategy;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

// Class-level JSON-B annotations don't seem to be working in the current Liberty beta
// @JsonbPropertyOrder(PropertyOrderStrategy.REVERSE)
// @JsonbTypeAdapter(RegistrationDetailsAdapter.class)
public class RegistrationDetails {

    @JsonbTransient
    private Long id;

    @JsonbDateFormat("dd.MM.yyyy HH:mm:ss")
    private ZonedDateTime created;

    // @JsonbProperty(value="registration_name", nillable = true)
    @NotNull
    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
