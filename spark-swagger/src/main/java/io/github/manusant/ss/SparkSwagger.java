package io.github.manusant.ss;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.github.manusant.ss.conf.IgnoreSpec;
import io.github.manusant.ss.conf.IpResolver;
import io.github.manusant.ss.conf.Options;
import io.github.manusant.ss.conf.TypifyProvider;
import io.github.manusant.ss.descriptor.EndpointDescriptor;
import io.github.manusant.ss.model.*;
import io.github.manusant.ss.model.auth.SecuritySchemeDefinition;
import io.github.manusant.ss.rest.Endpoint;
import io.github.manusant.ss.rest.EndpointResolver;
import lombok.extern.slf4j.Slf4j;
import spark.ExceptionHandler;
import spark.Filter;
import spark.HaltException;
import spark.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author manusant
 */
@Slf4j
public class SparkSwagger {

    public static final String CONF_FILE_NAME = "spark-swagger.conf";
    private final Swagger swagger;
    private final Service spark;
    private final Config config;
    private final Options options;
    private final String apiPath;

    private SparkSwagger(final Service spark, final Options options) {
        this.spark = spark;
        this.options = options == null ? Options.defaultOptions().build() : options;
        this.swagger = new Swagger();
        this.config = ConfigFactory.parseResources(Objects.requireNonNull(options).getConfPath());
        this.apiPath = this.config.getString("spark-swagger.basePath");
        this.swagger.setBasePath(this.apiPath);
        this.swagger.setExternalDocs(ExternalDocs.newBuilder().build());
        this.swagger.setHost(getHost());
        this.swagger.setInfo(getInfo());
        configDocRoute();
    }

    public String getApiPath() {
        return apiPath;
    }

    public Options getOptions() {
        return options;
    }

    public Service getSpark() {
        return spark;
    }

    public static SparkSwagger of(final Service spark) {
        return new SparkSwagger(spark, Options.defaultOptions().build());
    }

    public static SparkSwagger of(final Service spark, final Options options) {
        Objects.requireNonNull(options);
        return new SparkSwagger(spark, options);
    }

    private void configDocRoute() {
        String uiFolder = SwaggerHammer.getUiFolder(this.apiPath);
        SwaggerHammer.createDir(SwaggerHammer.getSwaggerUiFolder());
        SwaggerHammer.createDir(uiFolder);

        if (options.isEnableStaticMapping()) {
            enableStaticMapping(uiFolder);
        }

        if (options.isEnableCors()) {
            enableCors();
        }
    }

    private void enableCors() {
        // Enable CORS
        spark.options("/*",
                (request, response) -> {

                    String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers",
                                accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods",
                                accessControlRequestMethod);
                    }
                    return "OK";
                });

        spark.before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
        log.debug("Spark-Swagger: CORS enabled and allow Origin *");
    }

    private void enableStaticMapping(String uiFolder) {
        // Configure static mapping
        spark.externalStaticFileLocation(uiFolder);
        log.debug("Spark-Swagger: UI folder deployed at {}", uiFolder);
    }

    public SparkSwagger ignores(Supplier<IgnoreSpec> confSupplier) {
        this.swagger.ignores(confSupplier.get());
        TypifyProvider.setUp(confSupplier);
        return this;
    }

    public void generateDoc() throws IOException {
        new SwaggerSpecBuilder(swagger).build();
        new SwaggerHammer().prepareUi(config, swagger);
    }

    public ApiEndpoint getEndpoint(final String name) {
        if (swagger.getApiEndpoints() == null) return null;
        for (final ApiEndpoint ep : swagger.getApiEndpoints()) {
            if (name.equals(ep.getEndpointDescriptor().getTag().getName())) return ep;
        }
        return null;
    }

    public ApiEndpoint endpoint(final EndpointDescriptor.Builder descriptorBuilder) {
        return endpoint(descriptorBuilder, null);
    }

    public ApiEndpoint endpoint(final EndpointDescriptor.Builder descriptorBuilder, final Filter filter) {
        Optional.ofNullable(apiPath).orElseThrow(() -> new IllegalStateException("API Path must be specified in order to build REST endpoint"));
        EndpointDescriptor descriptor = descriptorBuilder.build();
        if (filter != null) spark.before(apiPath + descriptor.getPath() + "/*", filter);
        ApiEndpoint apiEndpoint = new ApiEndpoint(this, descriptor);
        this.swagger.addApiEndpoint(apiEndpoint);
        return apiEndpoint;
    }

    public SparkSwagger endpoint(final EndpointDescriptor.Builder descriptorBuilder, final Filter filter, Consumer<ApiEndpoint> endpointDef) {
        Optional.ofNullable(apiPath).orElseThrow(() -> new IllegalStateException("API Path must be specified in order to build REST endpoint"));
        EndpointDescriptor descriptor = descriptorBuilder.build();
        spark.before(apiPath + descriptor.getPath() + "/*", filter);
        ApiEndpoint apiEndpoint = new ApiEndpoint(this, descriptor);
        endpointDef.accept(apiEndpoint);
        this.swagger.addApiEndpoint(apiEndpoint);
        return this;
    }

    public SparkSwagger endpoint(final Endpoint endpoint) {
        Optional.ofNullable(endpoint).orElseThrow(() -> new IllegalStateException("API Endpoint cannot be null"));
        endpoint.bind(this);
        return this;
    }

    public SparkSwagger endpoints(final EndpointResolver resolver) {
        Optional.ofNullable(resolver).orElseThrow(() -> new IllegalStateException("API Endpoint Resolver cannot be null"));
        resolver.endpoints().forEach(this::endpoint);
        return this;
    }

    public SparkSwagger before(Filter filter) {
        spark.before(apiPath + "/*", filter);
        return this;
    }

    public SparkSwagger after(Filter filter) {
        spark.after(apiPath + "/*", filter);
        return this;
    }

    public synchronized SparkSwagger exception(Class<? extends Exception> exceptionClass, final ExceptionHandler handler) {
        spark.exception(exceptionClass, handler);
        return this;
    }

    public HaltException halt() {
        return spark.halt();
    }

    public HaltException halt(int status) {
        return spark.halt(status);
    }

    public HaltException halt(String body) {
        return spark.halt(body);
    }

    public HaltException halt(int status, String body) {
        return spark.halt(status, body);
    }

    public SparkSwagger security(String name, SecuritySchemeDefinition securityDefinition) {
        return security(name, securityDefinition, Collections.emptyList());
    }

    public SparkSwagger security(String name, SecuritySchemeDefinition securityDefinition, List<String> scopes) {
        swagger.addSecurityDefinition(name, securityDefinition);
        swagger.addSecurity(new SecurityRequirement().requirement(name, scopes));
        return this;
    }

    private String getHost() {
        String host = this.config.getString("spark-swagger.host");
        if (host == null || host.contains("localhost") && host.split(":").length != 2) {
            throw new IllegalArgumentException("Host is required. If host name is 'localhost' you also need to specify port");
        } else if (host.contains("localhost")) {
            String[] hostParts = host.split(":");
            host = IpResolver.resolvePublicIp() + ":" + hostParts[1];
        }
        log.debug("Spark-Swagger: Host resolved to {}", host);
        return host;
    }

    private Info getInfo() {
        Config infoConfig = Optional.ofNullable(config.getConfig("spark-swagger.info")).orElseThrow(() -> new IllegalArgumentException("'spark-swagger.info' configuration is required"));

        if (options.getVersion() == null) {
            // Resolve version from config if not supplied programmatically
            if (this.config.hasPath("spark-swagger.info.version")) {
                options.setVersion(this.config.getString("spark-swagger.info.version"));
            } else if (this.config.hasPath("spark-swagger.info.project.version")) {
                options.setVersion(this.config.getString("spark-swagger.info.project.version"));
            }
        }

        Config externalDocConf = this.config.getConfig("spark-swagger.info.externalDoc");
        if (externalDocConf != null) {
            ExternalDocs doc = ExternalDocs.newBuilder()
                    .withDescription(externalDocConf.getString("description"))
                    .withUrl(externalDocConf.getString("url"))
                    .build();
            swagger.setExternalDocs(doc);
        }

        Info info = new Info();
        info.description(infoConfig.getString("description"));
        info.version(options.getVersion());
        info.title(infoConfig.getString("title"));
        info.termsOfService(infoConfig.getString("termsOfService"));

        if (infoConfig.hasPath("schemes")) {
            List<String> schemeStrings = Optional.ofNullable(infoConfig.getStringList("schemes")).orElseThrow(() -> new IllegalArgumentException("'spark-swagger.info.schemes' configuration is required"));
            List<Scheme> schemes = schemeStrings.stream()
                    .filter(s -> Scheme.forValue(s) != null)
                    .map(Scheme::forValue)
                    .collect(Collectors.toList());
            if (schemes.isEmpty()) {
                throw new IllegalArgumentException("At least one Scheme mus be specified. Use 'spark-swagger.info.schemes' property. spark-swagger.info.schemes =[\"HTTP\"]");
            }
            swagger.schemes(schemes);
        }

        Config contactConfig = this.config.getConfig("spark-swagger.info.contact");
        if (contactConfig != null) {
            Contact contact = new Contact();
            contact.name(contactConfig.getString("name"));
            contact.email(contactConfig.getString("email"));
            contact.url(contactConfig.getString("url"));
            info.setContact(contact);
        }

        if (this.config.hasPath("spark-swagger.info.license")) {
            Config licenseConfig = this.config.getConfig("spark-swagger.info.license");
            if (licenseConfig != null) {
                License license = new License();
                license.name(licenseConfig.getString("name"));
                license.url(licenseConfig.getString("url"));
                info.setLicense(license);
            }
        }
        return info;
    }
}
