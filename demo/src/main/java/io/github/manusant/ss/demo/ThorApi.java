package io.github.manusant.ss.demo;

import io.github.manusant.ss.SparkSwagger;
import io.github.manusant.ss.conf.Options;
import io.github.manusant.ss.demo.endpoint.HammerEndpoint;
import io.github.manusant.ss.demo.endpoint.ShieldEndpoint;
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

        SparkSwagger.of(spark, options)
                .endpoints(() -> Arrays.asList(new HammerEndpoint(), new ShieldEndpoint()))
                .generateDoc();
    }
}
