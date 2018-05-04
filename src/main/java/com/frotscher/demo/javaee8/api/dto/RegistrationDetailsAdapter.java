package com.frotscher.demo.javaee8.api.dto;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.bind.adapter.JsonbAdapter;

// Adapters allow implementing more complex mappings than the JSON-B annotations,
// e.g. mapping two JSON fields to one Java field ("firstName", "lastName" -> "name")
public class RegistrationDetailsAdapter implements JsonbAdapter<RegistrationDetails, JsonObject> {
    @Override
    public JsonObject adaptToJson(RegistrationDetails details) throws Exception {
        return Json.createObjectBuilder()
                .add("registrationId", details.getId())
                .add("registrationName", details.getName())
                .build();
    }

    @Override
    public RegistrationDetails adaptFromJson(JsonObject jsonObject) throws Exception {
        RegistrationDetails details = new RegistrationDetails();
        details.setName(jsonObject.getString("name"));
        return details;
    }
}
