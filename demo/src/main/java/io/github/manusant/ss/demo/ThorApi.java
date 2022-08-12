package io.github.manusant.ss.demo;

import io.github.manusant.ss.SparkSwagger;
import io.github.manusant.ss.conf.Options;
import io.github.manusant.ss.demo.endpoint.HammerEndpoint;
import io.github.manusant.ss.demo.endpoint.ShieldEndpoint;
import io.github.manusant.ss.model.auth.*;
import spark.Service;

import java.io.IOException;
import java.util.Arrays;

public class ThorApi {

    public static void main(String[] args) throws IOException {

        Service spark = Service.ignite()
                .ipAddress("localhost")
                .port(8081);

        Options options =  Options.defaultOptions()
                .confPath("conf/" + SparkSwagger.CONF_FILE_NAME)
                .version("1.0.2")
                .build();

        BasicAuthDefinition basic = new BasicAuthDefinition();
        basic.setDescription("Basic Auth for Thor");

        ApiKeyAuthDefinition apiKey = new ApiKeyAuthDefinition();
        apiKey.in(In.HEADER);
        apiKey.setName("x-api-key");

        OAuth2Definition oauth = new OAuth2Definition();
        oauth.setAuthorizationUrl("http://petstore.swagger.io/oauth/dialog");
        oauth.setFlow("implicit");
        oauth.addScope("write:shield", "modify thor shields");
        oauth.addScope("read:shield", "read thor shields");

        SparkSwagger.of(spark, options)
                .endpoints(() -> Arrays.asList(new HammerEndpoint(), new ShieldEndpoint()))
                .security("thor_basic",basic)
                .security("thor_api_key",apiKey)
                .security("thor_auth",oauth)
                .generateDoc();
    }
}
