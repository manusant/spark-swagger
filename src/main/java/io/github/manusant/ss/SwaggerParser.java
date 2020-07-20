package io.github.manusant.ss;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author manusant
 */
public class SwaggerParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerParser.class);

    public static void parseYaml(final Swagger swagger, final String filePath) throws IOException {
        LOGGER.debug("Spark-Swagger: Start parsing Swagger definitions");
        // Create an ObjectMapper mapper for YAML
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.setSerializationInclusion(Include.NON_NULL);
        // Parse endpoints
        swagger.parse();
        // Write object as YAML file
        mapper.writeValue(new File(filePath), swagger);
        LOGGER.debug("Spark-Swagger: Swagger definitions saved as {} [YAML]", filePath);
    }

    public static void parseJson(final Swagger swagger, final String filePath) throws IOException {
        LOGGER.debug("Spark-Swagger: Start parsing Swagger definitions");
        // Create an ObjectMapper mapper for JSON
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        mapper.setSerializationInclusion(Include.NON_NULL);
        // Parse endpoints
        swagger.parse();
        mapper.writeValue(new File(filePath), swagger);
        LOGGER.debug("Spark-Swagger: Swagger definitions saved as " + filePath + " [JSON]");
    }

    public static void parseJs(final Swagger swagger, final String filePath) throws IOException {
        LOGGER.debug("Spark-Swagger: Start parsing Swagger definitions");
        // Create an ObjectMapper mapper for JSON
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        mapper.setSerializationInclusion(Include.NON_NULL);
        // Parse endpoints
        swagger.parse();

        String js = mapper.writeValueAsString(swagger);
        js = "window.swaggerSpec=" + js;

        File uiFolder = new File(filePath);
        uiFolder.delete();

        try (PrintWriter out = new PrintWriter(filePath)) {
            out.println(js);
        }
        LOGGER.debug("Spark-Swagger: Swagger definitions saved as " + filePath + " [JS]");
    }
}
