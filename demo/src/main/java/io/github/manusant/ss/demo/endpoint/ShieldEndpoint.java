package io.github.manusant.ss.demo.endpoint;

import com.beerboy.spark.typify.route.GsonRoute;
import com.beerboy.spark.typify.route.TypedGsonRoute;
import io.github.manusant.ss.SparkSwagger;
import io.github.manusant.ss.demo.model.BackupRequest;
import io.github.manusant.ss.demo.model.Shield;
import io.github.manusant.ss.rest.Endpoint;
import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;

import java.util.Arrays;

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

                .get(path("/")
                        .withDescription("Gets the available shields")
                        .withResponseAsCollection(Shield.class), new GsonRoute() {
                    @Override
                    public Object handleAndTransform(Request request, Response response) {

                        Shield shield = Shield.builder()
                                .id("sh_123456")
                                .name("Thor Main Shield")
                                .owner("Manuel Santos")
                                .defense(10)
                                .build();

                        return ok(response, Arrays.asList(shield));
                    }
                })

                .post(path("/:id")
                        .withDescription("Get Shield by ID")
                        .withRequestType(BackupRequest.class)
                        .withResponseType(Shield.class), new TypedGsonRoute<BackupRequest, Object>() {

                    @Override
                    public Object handleAndTransform(BackupRequest body, Request request, Response response) {
                        return badRequest(response, "Invalid shield ID");
                    }
                })

                .delete(path("/")
                        .withDescription("Delete all shields")
                        .withGenericResponse(), new GsonRoute() {
                    @Override
                    public Object handleAndTransform(Request request, Response response) {
                        return ok(response, "Thor Store successfully cleared");
                    }
                });
    }
}