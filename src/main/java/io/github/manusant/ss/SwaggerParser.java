package io.github.manusant.ss;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author manusant
 */
@Slf4j
public class SwaggerParser {

    public static void parseYaml(final Swagger swagger, final String filePath) throws IOException {
        log.debug("Spark-Swagger: Start parsing Swagger definitions");
        // Create an ObjectMapper mapper for YAML
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.setSerializationInclusion(Include.NON_NULL);

        // Write object as YAML file
        mapper.writeValue(new File(filePath), swagger);
        log.debug("Spark-Swagger: Swagger definitions saved as {} [YAML]", filePath);
    }

    public static void parseJson(final Swagger swagger, final String filePath) throws IOException {
        log.debug("Spark-Swagger: Start parsing Swagger definitions");
        // Create an ObjectMapper mapper for JSON
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        mapper.setSerializationInclusion(Include.NON_NULL);

        mapper.writeValue(new File(filePath), swagger);
        log.debug("Spark-Swagger: Swagger definitions saved as " + filePath + " [JSON]");
    }

    public static void parseJs(final Swagger swagger, final String filePath) throws IOException {
        log.debug("Spark-Swagger: Start parsing Swagger definitions");
        // Create an ObjectMapper mapper for JSON
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        mapper.setSerializationInclusion(Include.NON_NULL);

        String js = mapper.writeValueAsString(swagger);
        js = "window.swaggerSpec=" + js;

        File uiFolder = new File(filePath);
        uiFolder.delete();

        try (PrintWriter out = new PrintWriter(filePath)) {
            out.println(js);
        }
        log.debug("Spark-Swagger: Swagger definitions saved as " + filePath + " [JS]");
    }
}
