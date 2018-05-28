package com.beerboy.ss;

import com.beerboy.ss.descriptor.EndpointDescriptor;
import com.beerboy.ss.descriptor.MethodDescriptor;
import com.beerboy.ss.model.ContentType;
import com.beerboy.ss.model.HttpMethod;
import com.beerboy.spark.typify.route.GsonRoute;
import com.beerboy.spark.typify.route.TypedGsonRoute;
import spark.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author manusant
 */
public class ApiEndpoint {

    private SparkSwagger swagger;
    private EndpointDescriptor endpointDescriptor;
    private List<MethodDescriptor> methodDescriptors;

    public ApiEndpoint(final SparkSwagger swagger, final EndpointDescriptor endpointDescriptor) {
        this.swagger = swagger;
        this.endpointDescriptor = endpointDescriptor;
        this.endpointDescriptor.setNameSpace(swagger.getApiPath() + endpointDescriptor.getPath());
        this.methodDescriptors = new ArrayList<>();
    }

    public static ApiEndpoint of(final SparkSwagger swagger, final EndpointDescriptor endpointDescriptor) {
        return new ApiEndpoint(swagger, endpointDescriptor);
    }

    private MethodDescriptor bindDescription(HttpMethod method, MethodDescriptor.Builder descriptorBuilder, Route route) {
        Optional.ofNullable(descriptorBuilder).orElseThrow(() -> new IllegalArgumentException("Description is required"));
        MethodDescriptor descriptor = descriptorBuilder.build();
        descriptor.setMethod(method);
        descriptor.setPath(endpointDescriptor.getPath() + descriptor.getPath());
        methodDescriptors.add(descriptor);

        return descriptor;
    }

    private MethodDescriptor bindDescription(HttpMethod method, MethodDescriptor.Builder descriptorBuilder) {
        Optional.ofNullable(descriptorBuilder).orElseThrow(() -> new IllegalArgumentException("Description is required"));
        MethodDescriptor descriptor = descriptorBuilder.build();
        descriptor.setMethod(method);
        methodDescriptors.add(descriptor);
        return descriptor;
    }

    public SparkSwagger and() {
        return swagger;
    }

    public List<MethodDescriptor> getMethodDescriptors() {
        return methodDescriptors;
    }

    public EndpointDescriptor getEndpointDescriptor() {
        return endpointDescriptor;
    }

    public ApiEndpoint get(final MethodDescriptor.Builder descriptorBuilder, Route route) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.GET, descriptorBuilder, route);
        swagger.getSpark().get(swagger.getApiPath() + descriptor.getPath(), route);
        return this;
    }

    public ApiEndpoint post(final MethodDescriptor.Builder descriptorBuilder, Route route) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.POST, descriptorBuilder, route);
        swagger.getSpark().post(swagger.getApiPath() + descriptor.getPath(), route);
        return this;
    }

    public ApiEndpoint put(final MethodDescriptor.Builder descriptorBuilder, Route route) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.PUT, descriptorBuilder, route);
        swagger.getSpark().put(swagger.getApiPath() + descriptor.getPath(), route);
        return this;
    }

    public ApiEndpoint patch(final MethodDescriptor.Builder descriptorBuilder, Route route) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.PATCH, descriptorBuilder, route);
        swagger.getSpark().patch(swagger.getApiPath() + descriptor.getPath(), route);
        return this;
    }

    public ApiEndpoint delete(final MethodDescriptor.Builder descriptorBuilder, Route route) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.DELETE, descriptorBuilder, route);
        swagger.getSpark().delete(swagger.getApiPath() + descriptor.getPath(), route);
        return this;
    }

    public ApiEndpoint head(final MethodDescriptor.Builder descriptorBuilder, Route route) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.HEAD, descriptorBuilder, route);
        swagger.getSpark().head(swagger.getApiPath() + descriptor.getPath(), route);
        return this;
    }

    public ApiEndpoint trace(final MethodDescriptor.Builder descriptorBuilder, Route route) {
        MethodDescriptor descriptor = bindDescription(null, descriptorBuilder, route);
        swagger.getSpark().trace(swagger.getApiPath() + descriptor.getPath(), route);
        return this;
    }

    public ApiEndpoint connect(final MethodDescriptor.Builder descriptorBuilder, Route route) {
        MethodDescriptor descriptor = bindDescription(null, descriptorBuilder, route);
        swagger.getSpark().connect(swagger.getApiPath() + descriptor.getPath(), route);
        return this;
    }

    public ApiEndpoint options(final MethodDescriptor.Builder descriptorBuilder, Route route) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.OPTIONS, descriptorBuilder, route);
        swagger.getSpark().options(swagger.getApiPath() + descriptor.getPath(), route);
        return this;
    }

    public ApiEndpoint before(final MethodDescriptor.Builder descriptorBuilder, Filter filter) {
        MethodDescriptor descriptor = bindDescription(null, descriptorBuilder);
        swagger.getSpark().before(swagger.getApiPath() + descriptor.getPath(), filter);
        return this;
    }

    public ApiEndpoint after(final MethodDescriptor.Builder descriptorBuilder, Filter filter) {
        MethodDescriptor descriptor = bindDescription(null, descriptorBuilder);
        swagger.getSpark().after(swagger.getApiPath() + descriptor.getPath(), filter);
        return this;
    }

    public ApiEndpoint get(final MethodDescriptor.Builder descriptorBuilder, String acceptType, Route route) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.GET, descriptorBuilder, route);
        swagger.getSpark().get(swagger.getApiPath() + descriptor.getPath(), acceptType, route);
        return this;
    }

    public ApiEndpoint post(final MethodDescriptor.Builder descriptorBuilder, String acceptType, Route route) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.POST, descriptorBuilder, route);
        swagger.getSpark().post(swagger.getApiPath() + descriptor.getPath(), acceptType, route);
        return this;
    }

    public ApiEndpoint put(final MethodDescriptor.Builder descriptorBuilder, String acceptType, Route route) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.PUT, descriptorBuilder, route);
        swagger.getSpark().put(swagger.getApiPath() + descriptor.getPath(), acceptType, route);
        return this;
    }

    public ApiEndpoint patch(final MethodDescriptor.Builder descriptorBuilder, String acceptType, Route route) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.PATCH, descriptorBuilder, route);
        swagger.getSpark().patch(swagger.getApiPath() + descriptor.getPath(), acceptType, route);
        return this;
    }

    public ApiEndpoint delete(final MethodDescriptor.Builder descriptorBuilder, String acceptType, Route route) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.DELETE, descriptorBuilder, route);
        swagger.getSpark().delete(swagger.getApiPath() + descriptor.getPath(), acceptType, route);
        return this;
    }

    public ApiEndpoint head(final MethodDescriptor.Builder descriptorBuilder, String acceptType, Route route) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.HEAD, descriptorBuilder);
        swagger.getSpark().head(swagger.getApiPath() + descriptor.getPath(), acceptType, route);
        return this;
    }

    public ApiEndpoint trace(final MethodDescriptor.Builder descriptorBuilder, String acceptType, Route route) {
        MethodDescriptor descriptor = bindDescription(null, descriptorBuilder);
        swagger.getSpark().trace(swagger.getApiPath() + descriptor.getPath(), acceptType, route);
        return this;
    }

    public ApiEndpoint connect(final MethodDescriptor.Builder descriptorBuilder, String acceptType, Route route) {
        MethodDescriptor descriptor = bindDescription(null, descriptorBuilder);
        swagger.getSpark().connect(swagger.getApiPath() + descriptor.getPath(), acceptType, route);
        return this;
    }

    public ApiEndpoint options(final MethodDescriptor.Builder descriptorBuilder, String acceptType, Route route) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.OPTIONS, descriptorBuilder);
        swagger.getSpark().options(swagger.getApiPath() + descriptor.getPath(), acceptType, route);
        return this;
    }

    public ApiEndpoint before(Filter filter) {
        swagger.getSpark().before(filter);
        return this;
    }

    public ApiEndpoint after(Filter filter) {
        swagger.getSpark().after(filter);
        return this;
    }

    public ApiEndpoint before(final MethodDescriptor.Builder descriptorBuilder, String acceptType, Filter filter) {
        MethodDescriptor descriptor = bindDescription(null, descriptorBuilder);
        swagger.getSpark().before(swagger.getApiPath() + descriptor.getPath(), acceptType, filter);
        return this;
    }

    public ApiEndpoint after(final MethodDescriptor.Builder descriptorBuilder, String acceptType, Filter filter) {
        MethodDescriptor descriptor = bindDescription(null, descriptorBuilder);
        swagger.getSpark().after(swagger.getApiPath() + descriptor.getPath(), acceptType, filter);
        return this;
    }

    public ApiEndpoint afterAfter(Filter filter) {
        swagger.getSpark().afterAfter(filter);
        return this;
    }

    public ApiEndpoint afterAfter(final MethodDescriptor.Builder descriptorBuilder, Filter filter) {
        MethodDescriptor descriptor = bindDescription(null, descriptorBuilder);
        swagger.getSpark().afterAfter(swagger.getApiPath() + descriptor.getPath(), filter);
        return this;
    }

    public ApiEndpoint get(final MethodDescriptor.Builder descriptorBuilder, TemplateViewRoute route, TemplateEngine engine) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.GET, descriptorBuilder);
        swagger.getSpark().get(swagger.getApiPath() + descriptor.getPath(), route, engine);
        return this;
    }

    public ApiEndpoint get(final MethodDescriptor.Builder descriptorBuilder, String acceptType, TemplateViewRoute route, TemplateEngine engine) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.GET, descriptorBuilder);
        swagger.getSpark().get(swagger.getApiPath() + descriptor.getPath(), acceptType, route, engine);
        return this;
    }

    public ApiEndpoint post(final MethodDescriptor.Builder descriptorBuilder, TemplateViewRoute route, TemplateEngine engine) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.POST, descriptorBuilder);
        swagger.getSpark().post(swagger.getApiPath() + descriptor.getPath(), route, engine);
        return this;
    }

    public ApiEndpoint post(final MethodDescriptor.Builder descriptorBuilder, String acceptType, TemplateViewRoute route, TemplateEngine engine) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.POST, descriptorBuilder);
        swagger.getSpark().post(swagger.getApiPath() + descriptor.getPath(), acceptType, route, engine);
        return this;
    }

    public ApiEndpoint put(final MethodDescriptor.Builder descriptorBuilder, TemplateViewRoute route, TemplateEngine engine) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.PUT, descriptorBuilder);
        swagger.getSpark().put(swagger.getApiPath() + descriptor.getPath(), route, engine);
        return this;
    }

    public ApiEndpoint put(final MethodDescriptor.Builder descriptorBuilder, String acceptType, TemplateViewRoute route, TemplateEngine engine) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.PUT, descriptorBuilder);
        swagger.getSpark().put(swagger.getApiPath() + descriptor.getPath(), acceptType, route, engine);
        return this;
    }

    public ApiEndpoint delete(final MethodDescriptor.Builder descriptorBuilder, TemplateViewRoute route, TemplateEngine engine) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.DELETE, descriptorBuilder);
        swagger.getSpark().delete(swagger.getApiPath() + descriptor.getPath(), route, engine);
        return this;
    }

    public ApiEndpoint delete(final MethodDescriptor.Builder descriptorBuilder, String acceptType, TemplateViewRoute route, TemplateEngine engine) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.DELETE, descriptorBuilder);
        swagger.getSpark().delete(swagger.getApiPath() + descriptor.getPath(), acceptType, route, engine);
        return this;
    }

    public ApiEndpoint patch(final MethodDescriptor.Builder descriptorBuilder, TemplateViewRoute route, TemplateEngine engine) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.PATCH, descriptorBuilder);
        swagger.getSpark().patch(swagger.getApiPath() + descriptor.getPath(), route, engine);
        return this;
    }

    public ApiEndpoint patch(final MethodDescriptor.Builder descriptorBuilder, String acceptType, TemplateViewRoute route, TemplateEngine engine) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.PATCH, descriptorBuilder);
        swagger.getSpark().patch(swagger.getApiPath() + descriptor.getPath(), acceptType, route, engine);
        return this;
    }

    public ApiEndpoint head(final MethodDescriptor.Builder descriptorBuilder, TemplateViewRoute route, TemplateEngine engine) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.HEAD, descriptorBuilder);
        swagger.getSpark().head(swagger.getApiPath() + descriptor.getPath(), route, engine);
        return this;
    }

    public ApiEndpoint head(final MethodDescriptor.Builder descriptorBuilder, String acceptType, TemplateViewRoute route, TemplateEngine engine) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.HEAD, descriptorBuilder);
        swagger.getSpark().head(swagger.getApiPath() + descriptor.getPath(), acceptType, route, engine);
        return this;
    }

    public ApiEndpoint trace(final MethodDescriptor.Builder descriptorBuilder, TemplateViewRoute route, TemplateEngine engine) {
        MethodDescriptor descriptor = bindDescription(null, descriptorBuilder);
        swagger.getSpark().trace(swagger.getApiPath() + descriptor.getPath(), route, engine);
        return this;
    }

    public ApiEndpoint trace(final MethodDescriptor.Builder descriptorBuilder, String acceptType, TemplateViewRoute route, TemplateEngine engine) {
        MethodDescriptor descriptor = bindDescription(null, descriptorBuilder);
        swagger.getSpark().trace(swagger.getApiPath() + descriptor.getPath(), acceptType, route, engine);
        return this;
    }

    public ApiEndpoint connect(final MethodDescriptor.Builder descriptorBuilder, TemplateViewRoute route, TemplateEngine engine) {
        MethodDescriptor descriptor = bindDescription(null, descriptorBuilder);
        swagger.getSpark().connect(swagger.getApiPath() + descriptor.getPath(), route, engine);
        return this;
    }

    public ApiEndpoint connect(final MethodDescriptor.Builder descriptorBuilder, String acceptType, TemplateViewRoute route, TemplateEngine engine) {
        MethodDescriptor descriptor = bindDescription(null, descriptorBuilder);
        swagger.getSpark().connect(swagger.getApiPath() + descriptor.getPath(), acceptType, route, engine);
        return this;
    }

    public ApiEndpoint options(final MethodDescriptor.Builder descriptorBuilder, TemplateViewRoute route, TemplateEngine engine) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.OPTIONS, descriptorBuilder);
        swagger.getSpark().options(swagger.getApiPath() + descriptor.getPath(), route, engine);
        return this;
    }

    public ApiEndpoint options(final MethodDescriptor.Builder descriptorBuilder, String acceptType, TemplateViewRoute route, TemplateEngine engine) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.OPTIONS, descriptorBuilder);
        swagger.getSpark().options(swagger.getApiPath() + descriptor.getPath(), acceptType, route, engine);
        return this;
    }

    public ApiEndpoint get(final MethodDescriptor.Builder descriptorBuilder, Route route, ResponseTransformer transformer) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.GET, descriptorBuilder);
        swagger.getSpark().get(swagger.getApiPath() + descriptor.getPath(), route, transformer);
        return this;
    }

    public ApiEndpoint get(final MethodDescriptor.Builder descriptorBuilder, String acceptType, Route route, ResponseTransformer transformer) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.GET, descriptorBuilder, route);
        swagger.getSpark().get(swagger.getApiPath() + descriptor.getPath(), acceptType, route, transformer);
        return this;
    }

    public ApiEndpoint post(final MethodDescriptor.Builder descriptorBuilder, Route route, ResponseTransformer transformer) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.POST, descriptorBuilder, route);
        swagger.getSpark().post(swagger.getApiPath() + descriptor.getPath(), route, transformer);
        return this;
    }

    public ApiEndpoint post(final MethodDescriptor.Builder descriptorBuilder, String acceptType, Route route, ResponseTransformer transformer) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.POST, descriptorBuilder, route);
        swagger.getSpark().post(swagger.getApiPath() + descriptor.getPath(), acceptType, route, transformer);
        return this;
    }

    public ApiEndpoint put(final MethodDescriptor.Builder descriptorBuilder, Route route, ResponseTransformer transformer) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.PUT, descriptorBuilder, route);
        swagger.getSpark().put(swagger.getApiPath() + descriptor.getPath(), route, transformer);
        return this;
    }

    public ApiEndpoint put(final MethodDescriptor.Builder descriptorBuilder, String acceptType, Route route, ResponseTransformer transformer) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.PUT, descriptorBuilder, route);
        swagger.getSpark().put(swagger.getApiPath() + descriptor.getPath(), acceptType, route, transformer);
        return this;
    }

    public ApiEndpoint delete(final MethodDescriptor.Builder descriptorBuilder, Route route, ResponseTransformer transformer) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.DELETE, descriptorBuilder, route);
        swagger.getSpark().delete(swagger.getApiPath() + descriptor.getPath(), route, transformer);
        return this;
    }

    public ApiEndpoint delete(final MethodDescriptor.Builder descriptorBuilder, String acceptType, Route route, ResponseTransformer transformer) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.DELETE, descriptorBuilder, route);
        swagger.getSpark().delete(swagger.getApiPath() + descriptor.getPath(), acceptType, route, transformer);
        return this;
    }

    public ApiEndpoint head(final MethodDescriptor.Builder descriptorBuilder, Route route, ResponseTransformer transformer) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.HEAD, descriptorBuilder);
        swagger.getSpark().head(swagger.getApiPath() + descriptor.getPath(), route, transformer);
        return this;
    }

    public ApiEndpoint head(final MethodDescriptor.Builder descriptorBuilder, String acceptType, Route route, ResponseTransformer transformer) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.HEAD, descriptorBuilder);
        swagger.getSpark().head(swagger.getApiPath() + descriptor.getPath(), acceptType, route, transformer);
        return this;
    }

    public ApiEndpoint connect(final MethodDescriptor.Builder descriptorBuilder, Route route, ResponseTransformer transformer) {
        MethodDescriptor descriptor = bindDescription(null, descriptorBuilder);
        swagger.getSpark().connect(swagger.getApiPath() + descriptor.getPath(), route, transformer);
        return this;
    }

    public ApiEndpoint connect(final MethodDescriptor.Builder descriptorBuilder, String acceptType, Route route, ResponseTransformer transformer) {
        MethodDescriptor descriptor = bindDescription(null, descriptorBuilder);
        swagger.getSpark().connect(swagger.getApiPath() + descriptor.getPath(), acceptType, route, transformer);
        return this;
    }

    public ApiEndpoint trace(final MethodDescriptor.Builder descriptorBuilder, Route route, ResponseTransformer transformer) {
        MethodDescriptor descriptor = bindDescription(null, descriptorBuilder);
        swagger.getSpark().trace(swagger.getApiPath() + descriptor.getPath(), route, transformer);
        return this;
    }

    public ApiEndpoint trace(final MethodDescriptor.Builder descriptorBuilder, String acceptType, Route route, ResponseTransformer transformer) {
        MethodDescriptor descriptor = bindDescription(null, descriptorBuilder);
        swagger.getSpark().trace(swagger.getApiPath() + descriptor.getPath(), acceptType, route, transformer);
        return this;
    }

    public ApiEndpoint options(final MethodDescriptor.Builder descriptorBuilder, Route route, ResponseTransformer transformer) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.OPTIONS, descriptorBuilder);
        swagger.getSpark().options(swagger.getApiPath() + descriptor.getPath(), route, transformer);
        return this;
    }

    public ApiEndpoint options(final MethodDescriptor.Builder descriptorBuilder, String acceptType, Route route, ResponseTransformer transformer) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.OPTIONS, descriptorBuilder);
        swagger.getSpark().options(swagger.getApiPath() + descriptor.getPath(), acceptType, route, transformer);
        return this;
    }

    public ApiEndpoint patch(final MethodDescriptor.Builder descriptorBuilder, Route route, ResponseTransformer transformer) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.PATCH, descriptorBuilder);
        swagger.getSpark().patch(swagger.getApiPath() + descriptor.getPath(), route, transformer);
        return this;
    }

    public ApiEndpoint patch(final MethodDescriptor.Builder descriptorBuilder, String acceptType, Route route, ResponseTransformer transformer) {
        MethodDescriptor descriptor = bindDescription(HttpMethod.PATCH, descriptorBuilder);
        swagger.getSpark().patch(swagger.getApiPath() + descriptor.getPath(), acceptType, route, transformer);
        return this;
    }
}
