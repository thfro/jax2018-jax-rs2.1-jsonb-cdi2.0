package com.frotscher.demo.javaee8.api;

import com.frotscher.demo.javaee8.api.dto.RegistrationDetails;
import com.frotscher.demo.javaee8.services.RegistrationService;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonPatch;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.net.URI;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.logging.Logger;

@Path("registrations")
public class RegistrationResource {

    private Logger log = Logger.getLogger(RegistrationResource.class.getName());

    @Inject
    private RegistrationService registrationService;

    @Resource
    ManagedExecutorService mes;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRegistrations() {
        Collection<RegistrationDetails> allRegistrations = registrationService.getRegistrations();

        GenericEntity<Collection<RegistrationDetails>> ge
                = new GenericEntity<Collection<RegistrationDetails>>(allRegistrations) {};

        log.info("Returning list of all registrations");
        return Response.ok().entity(ge).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<Response> createRegistrationAsync(@Valid RegistrationDetails regDetails) {

        log.info("HTTP request handled by thread " + Thread.currentThread().getName());
        final CompletableFuture<Response> cf = new CompletableFuture<>();

        mes.execute(
            () -> {
                log.info("Business logic handled by thread " + Thread.currentThread().getName());
                Long regId = registrationService.createRegistration(regDetails);
                Response response = Response.created(URI.create("/registrations/" + regId)).build();
                cf.complete(response);
            });

        return cf;
    }

    @PATCH
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON_PATCH_JSON)
    public Response patchProject(@PathParam("id") Long registrationId, JsonArray json) {

        RegistrationDetails details = registrationService.getRegistration(registrationId);

        JsonPatch patch = Json.createPatch(json);

        details = patch(details, patch);
        registrationService.updateRegistration(registrationId, details);

        return Response.noContent().build();
    }

    private RegistrationDetails patch(RegistrationDetails details, JsonPatch patch) {
        Jsonb jsonb = JsonbBuilder.create();
        String detailsJsonString = jsonb.toJson(details);
        log.info("Entity before patching: " + detailsJsonString);

        JsonObject entityJsonObject = Json.createReader(new StringReader(detailsJsonString)).readObject();
        JsonObject patchedEntityObject = patch.apply(entityJsonObject);

        String patchedEntityJsonString = patchedEntityObject.toString();
        log.info("Entity after patching: " + patchedEntityJsonString);

        details = jsonb.fromJson(patchedEntityJsonString, RegistrationDetails.class);
        return details;
    }

    /* Patch can also be implemented in a generic way, i.e. once for any DTO class

    private <T> T patchGeneric(T dto, JsonPatch patch, Class<T> dtoClass) {
        Jsonb jsonb = JsonbBuilder.create();

        String dtoJsonString = jsonb.toJson(dto);
        log.info("DTO before patching: " + dtoJsonString);

        JsonObject dtoJsonObject = Json.createReader(new StringReader(dtoJsonString)).readObject();
        JsonObject patchedDtoJsonObject = patch.apply(dtoJsonObject);

        String patchedDtoJsonString = patchedDtoJsonObject.toString();
        log.info("DTO after patching: " + patchedDtoJsonString);

        dto = jsonb.fromJson(patchedDtoJsonString, dtoClass);
        return dto;
    }
    */
}
