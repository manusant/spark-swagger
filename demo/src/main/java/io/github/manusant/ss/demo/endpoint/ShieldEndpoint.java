package io.github.manusant.ss.demo.endpoint;

import io.github.manusant.ss.SparkSwagger;
import io.github.manusant.ss.demo.model.BackupRequest;
import io.github.manusant.ss.demo.model.Shield;
import io.github.manusant.ss.rest.Endpoint;
import io.github.manusant.ss.route.Route;
import io.github.manusant.ss.route.TypedRoute;
import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static io.github.manusant.ss.descriptor.EndpointDescriptor.endpointPath;
import static io.github.manusant.ss.descriptor.MethodDescriptor.path;
import static io.github.manusant.ss.rest.RestResponse.badRequest;
import static io.github.manusant.ss.rest.RestResponse.ok;

@Slf4j
public class ShieldEndpoint implements Endpoint {

    private static final String NAME_SPACE = "/shield";

    @Override
    public void bind(final SparkSwagger restApi) {

        restApi.endpoint(endpointPath(NAME_SPACE)
                        .withDescription("REST API for Thor Shields"), (q, a) -> log.info("Received request for Shield Rest API"))

                .get(path()
                        .withDescription("Gets the available shields")
                        .withSecurity("thor_basic")
                        .withResponseAsCollection(Shield.class), new Route() {
                    @Override
                    public Object onRequest(Request request, Response response) {

                        Shield shield = Shield.builder()
                                .id("sh_123456")
                                .name("Thor Main Shield")
                                .owner("Manuel Santos")
                                .defense(10)
                                .build();

                        return ok(response, Arrays.asList(shield));
                    }
                })

                .get(path("/options")
                        .withDescription("Gets all shield options")
                        .withSecurity("thor_api_key")
                        .withHeaderParam().withName("x-export-kpi").withDescription("Export KPI Header").withRequired(true)
                        .and()
                        .withCookieParam().withName("my-cookie-data")
                        .and()
                        .withResponseAsMap(Shield.class), new Route() {
                    @Override
                    public Object onRequest(Request request, Response response) {

                        Map<String, Shield> shields = new HashMap<>();

                        Shield thor = Shield.builder()
                                .id("sh_123456")
                                .name("Thor Shield")
                                .owner("Manuel Santos")
                                .defense(50)
                                .build();
                        shields.put("thor",thor);

                        Shield loki = Shield.builder()
                                .id("sh_255678")
                                .name("Loki Shield")
                                .owner("Manuel Santos")
                                .defense(20)
                                .build();
                        shields.put("loki",loki);

                        return ok(response, shields);
                    }
                })
                .post(path("/:id")
                        .withDescription("Get Shield by ID")
                        .withSecurity("thor_auth", Collections.singletonList("write:shield"))
                        .withRequestType(BackupRequest.class)
                        .withResponseType(Shield.class), new TypedRoute<BackupRequest>() {

                    @Override
                    public Object onRequest(BackupRequest body, Request request, Response response) {
                        return badRequest(response, "Invalid shield ID");
                    }
                })

                .delete(path("/")
                        .withDescription("Delete all shields")
                        .withSecurity("thor_auth", Collections.singletonList("write:shield"))
                        .withGenericResponse(), new Route() {
                    @Override
                    public Object onRequest(Request request, Response response) {
                        return ok(response, "Thor Store successfully cleared");
                    }
                });
    }
}
