package io.github.manusant.ss;

import io.github.manusant.ss.descriptor.MethodDescriptor;
import io.github.manusant.ss.descriptor.ParameterDescriptor;
import io.github.manusant.ss.factory.DefinitionsFactory;
import io.github.manusant.ss.factory.ParamsFactory;
import io.github.manusant.ss.model.Model;
import io.github.manusant.ss.model.Operation;
import io.github.manusant.ss.model.RefModel;
import io.github.manusant.ss.model.Response;
import io.github.manusant.ss.model.parameters.BodyParameter;
import io.github.manusant.ss.model.parameters.Parameter;
import io.github.manusant.ss.model.properties.ArrayProperty;
import io.github.manusant.ss.model.properties.MapProperty;
import io.github.manusant.ss.model.properties.Property;
import io.github.manusant.ss.model.utils.PropertyModelConverter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author santoman
 */
@Slf4j
public class SwaggerSpecBuilder {

    private final Swagger swagger;

    public SwaggerSpecBuilder(Swagger swagger) {
        this.swagger = swagger;
    }

    public void build() {
        log.debug("Spark-Swagger: Start parsing metadata");
        if (swagger.getApiEndpoints() != null) {
            swagger.getApiEndpoints().forEach(endpoint -> {
                if (swagger.getIgnoreSpec() == null || !swagger.getIgnoreSpec().ignored(endpoint.getEndpointDescriptor().getPath())) {

                    swagger.tag(endpoint.getEndpointDescriptor().getTag());
                    endpoint.getMethodDescriptors()
                            .forEach(methodDescriptor -> {

                                Operation op = new Operation();
                                op.tag(endpoint.getEndpointDescriptor().getTag().getName());
                                op.description(methodDescriptor.getDescription());
                                op.setSecurity(methodDescriptor.getSecurity());

                                Optional.ofNullable(methodDescriptor.getSummary()).ifPresent(op::summary);
                                Optional.ofNullable(methodDescriptor.getOperationId()).ifPresent(op::operationId);

                                List<Parameter> parameters = ParamsFactory.create(methodDescriptor.getPath(), methodDescriptor.getParameters());
                                op.setParameters(parameters);

                                // Supply Ignore configurations
                                DefinitionsFactory.ignoreSpec = swagger.getIgnoreSpec();

                                final ParameterDescriptor methodBody = methodDescriptor.getBody();
                                if (methodBody != null && methodBody.getModel() != null) {
                                    buildRequest(methodDescriptor, op, methodBody);
                                } else if (methodDescriptor.getRequestType() != null) {
                                    buildRequestFromType(methodDescriptor, op, methodBody);
                                }

                                final Map<String, Response> responses = methodDescriptor.getResponses();
                                if (responses != null && !responses.isEmpty()) {
                                    buildResponses(methodDescriptor, op, responses);
                                } else if (methodDescriptor.getResponseType() != null) {
                                    Response responseBody = buildResponseFromType(methodDescriptor);
                                    op.addResponse("200", responseBody);
                                } else {
                                    Response responseBody = new Response();
                                    responseBody.description("successful operation");
                                    op.addResponse("200", responseBody);
                                }

                                if (methodDescriptor.getProduces() != null) {
                                    op.produces(methodDescriptor.getProduces());
                                }
                                if (methodDescriptor.getConsumes() != null) {
                                    op.consumes(methodDescriptor.getConsumes());
                                }

                                swagger.addOperation(methodDescriptor.getPath(), methodDescriptor.getMethod(), op);
                            });
                }
            });
            log.debug("Spark-Swagger: metadata successfully parsed");
        } else {
            log.debug("Spark-Swagger: No metadata to parse. Please check your SparkSwagger configurations and Endpoints Resolver");
        }
    }

    private void buildRequest(MethodDescriptor methodDescriptor, Operation op, ParameterDescriptor methodBody) {
        final Model model = methodBody.getModel();
        swagger.addDefinition(model.getTitle(), model);

        BodyParameter requestBody = createBodyParameter(methodBody, model);
        op.addParameter(requestBody);

        if (methodDescriptor.getRequestType() != null) {
            swagger.addDefinitionsForType(model.getTitle(), methodDescriptor.getRequestType());
        }
    }

    private void buildRequestFromType(MethodDescriptor methodDescriptor, Operation op, ParameterDescriptor methodBody) {
        // Process fields
        Map<String, Model> definitions = DefinitionsFactory.create(methodDescriptor.getRequestType());
        for (String key : definitions.keySet()) {
            if (!swagger.hasDefinition(key)) {
                swagger.addDefinition(key, definitions.get(key));
            }
        }

        Model model;
        if (definitions.isEmpty()) {
            Property property = DefinitionsFactory.createProperty(null, methodDescriptor.getRequestType());
            model = new PropertyModelConverter().propertyToModel(property);
        } else {
            RefModel refModel = new RefModel();
            refModel.set$ref(methodDescriptor.getRequestType().getSimpleName());
            model = refModel;
        }

        BodyParameter requestBody = createBodyParameter(methodBody, model);
        op.addParameter(requestBody);
    }

    private Response buildResponseFromType(MethodDescriptor methodDescriptor) {
        PropertyModelConverter propertyModelConverter = new PropertyModelConverter();

        final Property property = typeToProperty(methodDescriptor.getResponseType());

        Response responseBody = new Response();
        responseBody.description("successful operation");

        if (methodDescriptor.isResponseAsCollection()) {
            ArrayProperty arrayProperty = new ArrayProperty();
            arrayProperty.setItems(property);
            responseBody.setSchema(arrayProperty);
            responseBody.setResponseSchema(propertyModelConverter.propertyToModel(arrayProperty));
        } else if (methodDescriptor.isResponseAsMap()) {
            MapProperty mapProperty = new MapProperty(property);
            mapProperty.additionalProperties(property);
            responseBody.setSchema(mapProperty);
            responseBody.setResponseSchema(propertyModelConverter.propertyToModel(mapProperty));
        } else {
            responseBody.setSchema(property);
            responseBody.setResponseSchema(propertyModelConverter.propertyToModel(property));
        }
        return responseBody;
    }

    private void buildResponses(MethodDescriptor methodDescriptor, Operation op, Map<String, Response> responses) {
        responses.forEach((code, response) -> {

            if (response.getResponseSchema() != null) {
                final Model resModel = response.getResponseSchema();
                swagger.addDefinition(resModel.getTitle(), resModel);

                if (resModel.getTypeClass() != null) {
                    swagger.addDefinitionsForType(resModel.getTitle(), resModel.getTypeClass());
                }
            } else if (response.getSchema() != null) {
                final Property resProp = response.getSchema();
                final Model resModel = new PropertyModelConverter().propertyToModel(resProp);
                response.setResponseSchema(resModel);
                swagger.addDefinition(resProp.getTitle(), resModel);

                if (resModel.getTypeClass() != null) {
                    swagger.addDefinitionsForType(resModel.getTitle(), resModel.getTypeClass());
                }
            } else if (response.getExamples() != null) {

                response.getExamples().forEach((exKey, example) -> {

                    final Map<String, Model> definitions = DefinitionsFactory.create(example.getClass());
                    definitions.forEach((defKey, defModel) -> {
                        if (!swagger.hasDefinition(defKey)) {
                            swagger.addDefinition(defKey, defModel);
                        }
                    });

                    final Property property;
                    if (definitions.isEmpty()) {
                        property = DefinitionsFactory.createProperty(null, example.getClass());
                    } else {
                        RefModel refModel = new RefModel();
                        refModel.set$ref(example.getClass().getSimpleName());
                        property = new PropertyModelConverter().modelToProperty(refModel);
                    }
                    property.setExample(example);
                    response.setSchema(property);
                    response.setResponseSchema(new PropertyModelConverter().propertyToModel(property));
                });
            } else if (methodDescriptor.getResponseType() != null) {
                final Model resTypeModel = typeToModel(methodDescriptor.getResponseType());
                response.setResponseSchema(resTypeModel);
            }

            op.addResponse(code, response);
        });
    }

    private BodyParameter createBodyParameter(ParameterDescriptor methodBody, Model model) {
        BodyParameter requestBody = new BodyParameter();
        requestBody.name(methodBody != null ? methodBody.getName() : "body");
        requestBody.description(methodBody != null ? methodBody.getDescription() : "Body object description");
        requestBody.setRequired(methodBody == null || methodBody.isRequired());
        requestBody.setSchema(model);
        return requestBody;
    }

    private Model typeToModel(final Class<?> type) {
        final Map<String, Model> definitions = swagger.addDefinitionsForType(null, type);

        final Model model;
        if (definitions.isEmpty()) {
            Property property = DefinitionsFactory.createProperty(null, type);
            model = new PropertyModelConverter().propertyToModel(property);
        } else {
            final RefModel refModel = new RefModel();
            refModel.set$ref(type.getSimpleName());
            model = refModel;
        }
        return model;
    }

    private Property typeToProperty(final Class<?> type) {
        final Map<String, Model> definitions = swagger.addDefinitionsForType(null, type);

        final Property property;
        if (definitions.isEmpty()) {
            property = DefinitionsFactory.createProperty(null, type);
        } else {
            final RefModel refModel = new RefModel();
            refModel.set$ref(type.getSimpleName());
            property = new PropertyModelConverter().modelToProperty(refModel);
        }
        return property;
    }
}
