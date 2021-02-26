package io.github.manusant.ss;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.github.manusant.spark.typify.provider.TypifyProvider;
import io.github.manusant.spark.typify.spec.IgnoreSpec;
import io.github.manusant.ss.conf.IpResolver;
import io.github.manusant.ss.conf.VersionResolver;
import io.github.manusant.ss.descriptor.EndpointDescriptor;
import io.github.manusant.ss.model.*;
import io.github.manusant.ss.rest.Endpoint;
import io.github.manusant.ss.rest.EndpointResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ExceptionHandler;
import spark.Filter;
import spark.HaltException;
import spark.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author manusant
 */
public class SparkSwagger {

    private static final Logger LOGGER = LoggerFactory.getLogger(SparkSwagger.class);

    public static final String CONF_FILE_NAME = "spark-swagger.conf";
    private final String apiPath;
    private final Swagger swagger;
    private final Service spark;
    private final Config config;
    private String version;

    private SparkSwagger(final Service spark, final String confPath, final String version) {
        this(spark, confPath, version, true, true);
    }

    private SparkSwagger(final Service spark, final String confPath, final String version, final boolean configStaticMapping, final boolean enableCors) {
        this.spark = spark;
        this.version = version;
        this.swagger = new Swagger();
        this.config = ConfigFactory.parseResources(confPath != null ? confPath : SparkSwagger.CONF_FILE_NAME);
        this.apiPath = this.config.getString("spark-swagger.basePath");
        this.swagger.setBasePath(this.apiPath);
        this.swagger.setExternalDocs(ExternalDocs.newBuilder().build());
        this.swagger.setHost(getHost());
        this.swagger.setInfo(getInfo());
        configDocRoute(configStaticMapping, enableCors);
    }

    private void configDocRoute(final boolean configStaticMapping, final boolean enableCors) {
        String uiFolder = SwaggerHammer.getUiFolder(this.apiPath);
        SwaggerHammer.createDir(SwaggerHammer.getSwaggerUiFolder());
        SwaggerHammer.createDir(uiFolder);

        if (configStaticMapping) {
            // Configure static mapping
            spark.externalStaticFileLocation(uiFolder);
            LOGGER.debug("Spark-Swagger: UI folder deployed at {}", uiFolder);
        }

        if (enableCors) {
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
            LOGGER.debug("Spark-Swagger: CORS enabled and allow Origin *");
        }
    }

    public String getApiPath() {
        return apiPath;
    }

    public String getVersion() {
        return version;
    }

    public Service getSpark() {
        return spark;
    }

    public static SparkSwagger of(final Service spark) {
        return new SparkSwagger(spark, null, null);
    }

    public static SparkSwagger of(final Service spark, final String confPath) {
        return new SparkSwagger(spark, confPath, null);
    }

    public static SparkSwagger of(final Service spark, final String confPath, final String version) {
        return new SparkSwagger(spark, confPath, version);
    }

    public static SparkSwagger of(final Service spark, final String confPath, final String version, final boolean configStaticMapping, final boolean enableCors) {
        return new SparkSwagger(spark, confPath, version, configDocRoute, enableCors);
    }

    public SparkSwagger ignores(Supplier<IgnoreSpec> confSupplier) {
        this.swagger.ignores(confSupplier.get());
        TypifyProvider.setUp(confSupplier.get());
        return this;
    }

    public void generateDoc() throws IOException {
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

    private String getHost() {
        String host = this.config.getString("spark-swagger.host");
        if (host == null || host.contains("localhost") && host.split(":").length != 2) {
            throw new IllegalArgumentException("Host is required. If host name is 'localhost' you also need to specify port");
        } else if (host.contains("localhost")) {
            String[] hostParts = host.split(":");
            host = IpResolver.resolvePublicIp() + ":" + hostParts[1];
        }
        LOGGER.debug("Spark-Swagger: Host resolved to {}", host);
        return host;
    }

    private Info getInfo() {
        Config infoConfig = Optional.ofNullable(config.getConfig("spark-swagger.info")).orElseThrow(() -> new IllegalArgumentException("'spark-swagger.info' configuration is required"));

        if (version == null) {
            Config projectConfig = config.getConfig("spark-swagger.info.project");
            if (projectConfig != null) {
                version = VersionResolver.resolveVersion(projectConfig.getString("groupId"), projectConfig.getString("artifactId"));
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
        info.version(version);
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
