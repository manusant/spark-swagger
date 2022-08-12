package io.github.manusant.ss.demo.endpoint;

import io.github.manusant.ss.SparkSwagger;
import io.github.manusant.ss.annotation.Content;
import io.github.manusant.ss.demo.model.BackupRequest;
import io.github.manusant.ss.demo.model.Network;
import io.github.manusant.ss.model.ContentType;
import io.github.manusant.ss.rest.Endpoint;
import io.github.manusant.ss.route.Route;
import io.github.manusant.ss.route.TypedRoute;
import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;

import java.util.Arrays;
import java.util.Collections;

import static io.github.manusant.ss.descriptor.EndpointDescriptor.endpointPath;
import static io.github.manusant.ss.descriptor.MethodDescriptor.path;
import static io.github.manusant.ss.rest.RestResponse.badRequest;
import static io.github.manusant.ss.rest.RestResponse.ok;

@Slf4j
public class HammerEndpoint implements Endpoint {

    private static final String NAME_SPACE = "/hammer";

    @Override
    public void bind(final SparkSwagger restApi) {

        restApi.endpoint(endpointPath(NAME_SPACE)
                        .withDescription("Hammer REST API exposing all Thor utilities "), (q, a) -> log.info("Received request for Hammer Rest API"))

                .get(path("/export")
                        .withDescription("Gets the whole Network")
                        .withResponseType(Network.class), new Route() {
                    @Override
                    public Object onRequest(Request request, Response response) {

                        Network network = Network.builder()
                                .id("thor_1111")
                                .name("Thor Network")
                                .owner("Manuel Santos")
                                .nodes(10000)
                                .build();

                        return ok(response, network);
                    }
                })

                .post(path("/backup")
                                .withDescription("Trigger Network Backup")
                                .withRequestType(BackupRequest.class)
                                .withGenericResponse(),
                        new TypedRoute<BackupRequest>() {

                            @Content(ContentType.APPLICATION_JSON)
                            public Object onRequest(BackupRequest body, Request request, Response response) {
                                return badRequest(response, "Backup Name required in order to backup Network Data");
                            }
                        })

                .delete(path("/")
                        .withDescription("Clear Thor network resources")
                        .withGenericResponse(), new Route() {
                    @Override
                    public Object onRequest(Request request, Response response) {
                        return ok(response, "Thor Store successfully cleared");
                    }
                });
    }
}
