package io.github.manusant.ss;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.manusant.spark.typify.spec.IgnoreSpec;
import io.github.manusant.ss.descriptor.ParameterDescriptor;
import io.github.manusant.ss.factory.DefinitionsFactory;
import io.github.manusant.ss.factory.ParamsFactory;
import io.github.manusant.ss.model.*;
import io.github.manusant.ss.model.auth.SecuritySchemeDefinition;
import io.github.manusant.ss.model.parameters.BodyParameter;
import io.github.manusant.ss.model.parameters.Parameter;
import io.github.manusant.ss.model.properties.Property;
import io.github.manusant.ss.model.utils.PropertyModelConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author manusant
 */
@JsonInclude(Include.NON_NULL)
public class Swagger {

    @JsonIgnore
    private static final Logger LOGGER = LoggerFactory.getLogger(Swagger.class);

    private String swagger = "2.0";
    private Info info;
    private String host;
    private String basePath;
    private List<Tag> tags;
    private List<Scheme> schemes;
    private List<String> consumes;
    private List<String> produces;
    private List<SecurityRequirement> security;
    private Map<String, Path> paths;
    private Map<String, SecuritySchemeDefinition> securityDefinitions;
    private Map<String, Model> definitions;
    private ExternalDocs externalDocs;
    private Map<String, Parameter> parameters;
    private Map<String, Response> responses;
    private Map<String, Object> vendorExtensions;
    @JsonIgnore
    private List<ApiEndpoint> apiEndpoints;
    @JsonIgnore
    private IgnoreSpec ignoreSpec;

    public Swagger endpoints(List<ApiEndpoint> apiEndpoints) {
        this.apiEndpoints = apiEndpoints;
        return this;
    }

    public Swagger info(Info info) {
        this.setInfo(info);
        return this;
    }

    public Swagger host(String host) {
        this.setHost(host);
        return this;
    }

    public Swagger basePath(String basePath) {
        this.setBasePath(basePath);
        return this;
    }

    public Swagger externalDocs(ExternalDocs value) {
        this.setExternalDocs(value);
        return this;
    }

    public Swagger tags(List<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public Swagger tag(Tag tag) {
        this.addTag(tag);
        return this;
    }

    public Swagger schemes(List<Scheme> schemes) {
        this.setSchemes(schemes);
        return this;
    }

    public Swagger scheme(Scheme scheme) {
        this.addScheme(scheme);
        return this;
    }

    public Swagger consumes(List<String> consumes) {
        this.setConsumes(consumes);
        return this;
    }

    public Swagger consumes(String consumes) {
        this.addConsumes(consumes);
        return this;
    }

    public Swagger produces(List<String> produces) {
        this.setProduces(produces);
        return this;
    }

    public Swagger produces(String produces) {
        this.addProduces(produces);
        return this;
    }

    public Swagger paths(Map<String, Path> paths) {
        this.setPaths(paths);
        return this;
    }

    public Swagger path(String key, Path path) {
        if (this.paths == null) {
            this.paths = new LinkedHashMap<>();
        }
        this.paths.put(key, path);
        return this;
    }

    public Swagger responses(Map<String, Response> responses) {
        this.responses = responses;
        return this;
    }

    public Swagger response(String key, Response response) {
        if (this.responses == null) {
            this.responses = new LinkedHashMap<>();
        }
        this.responses.put(key, response);
        return this;
    }

    public Swagger parameter(String key, Parameter parameter) {
        this.addParameter(key, parameter);
        return this;
    }

    public Swagger securityDefinition(String name, SecuritySchemeDefinition securityDefinition) {
        this.addSecurityDefinition(name, securityDefinition);
        return this;
    }

    public Swagger model(String name, Model model) {
        this.addDefinition(name, model);
        return this;
    }

    public Swagger security(SecurityRequirement securityRequirement) {
        this.addSecurity(securityRequirement);
        return this;
    }

    public Swagger ignores(IgnoreSpec ignoreConf) {
        this.ignoreSpec = ignoreConf;
        return this;
    }

    public Swagger vendorExtension(String key, Object extension) {
        if (this.vendorExtensions == null) {
            this.vendorExtensions = new LinkedHashMap<>();
        }
        this.vendorExtensions.put(key, extension);
        return this;
    }

    // getter & setters
    public String getSwagger() {
        return swagger;
    }

    public void setSwagger(String swagger) {
        this.swagger = swagger;
    }

    public List<ApiEndpoint> getApiEndpoints() {
        return apiEndpoints;
    }

    public void addApiEndpoint(ApiEndpoint endpoint) {
        if (apiEndpoints == null) {
            apiEndpoints = new ArrayList<>();
        }
        apiEndpoints.add(endpoint);
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public List<Scheme> getSchemes() {
        return schemes;
    }

    public void setSchemes(List<Scheme> schemes) {
        this.schemes = schemes;
    }

    public void addScheme(Scheme scheme) {
        if (schemes == null) {
            schemes = new ArrayList<>();
        }
        if (!schemes.contains(scheme)) {
            schemes.add(scheme);
        }
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Tag getTag(String tagName) {
        Tag tag = null;
        if (this.tags != null && tagName != null) {
            for (Tag existing : this.tags) {
                if (existing.getName().equals(tagName)) {
                    tag = existing;
                    break;
                }
            }
        }
        return tag;
    }

    public void addTag(Tag tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        if (tag != null && tag.getName() != null) {
            if (getTag(tag.getName()) == null) {
                this.tags.add(tag);
            }
        }
    }

    public List<String> getConsumes() {
        return consumes;
    }

    public void setConsumes(List<String> consumes) {
        this.consumes = consumes;
    }

    public void addConsumes(String consumes) {
        if (this.consumes == null) {
            this.consumes = new ArrayList<>();
        }

        if (!this.consumes.contains(consumes)) {
            this.consumes.add(consumes);
        }
    }

    public List<String> getProduces() {
        return produces;
    }

    public void setProduces(List<String> produces) {
        this.produces = produces;
    }

    public void addProduces(String produces) {
        if (this.produces == null) {
            this.produces = new ArrayList<>();
        }

        if (!this.produces.contains(produces)) {
            this.produces.add(produces);
        }
    }

    public Map<String, Path> getPaths() {
        return paths;
    }

    public void setPaths(Map<String, Path> paths) {
        this.paths = paths;
    }

    public Path getPath(String path) {
        if (this.paths == null) {
            return null;
        }
        return this.paths.get(path);
    }

    public Map<String, SecuritySchemeDefinition> getSecurityDefinitions() {
        return securityDefinitions;
    }

    public void setSecurityDefinitions(Map<String, SecuritySchemeDefinition> securityDefinitions) {
        this.securityDefinitions = securityDefinitions;
    }

    public void addSecurityDefinition(String name, SecuritySchemeDefinition securityDefinition) {
        if (this.securityDefinitions == null) {
            this.securityDefinitions = new LinkedHashMap<>();
        }
        this.securityDefinitions.put(name, securityDefinition);
    }

    /**
     * @deprecated Use {@link #getSecurity()}.
     */
    @JsonIgnore
    @Deprecated
    public List<SecurityRequirement> getSecurityRequirement() {
        return security;
    }

    /**
     * @deprecated Use {@link #setSecurity(List)}.
     */
    @JsonIgnore
    @Deprecated
    public void setSecurityRequirement(List<SecurityRequirement> securityRequirements) {
        this.security = securityRequirements;
    }

    /**
     * @deprecated Use {@link #addSecurity(SecurityRequirement)}.
     */
    @JsonIgnore
    @Deprecated
    public void addSecurityDefinition(SecurityRequirement securityRequirement) {
        this.addSecurity(securityRequirement);
    }

    public List<SecurityRequirement> getSecurity() {
        return security;
    }

    public void setSecurity(List<SecurityRequirement> securityRequirements) {
        this.security = securityRequirements;
    }

    public void addSecurity(SecurityRequirement securityRequirement) {
        if (this.security == null) {
            this.security = new ArrayList<SecurityRequirement>();
        }
        this.security.add(securityRequirement);
    }

    public Map<String, Model> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(Map<String, Model> definitions) {
        this.definitions = definitions;
    }

    public void addDefinition(String key, Model model) {
        if (key == null) return;
        if (this.definitions == null) {
            this.definitions = new LinkedHashMap<String, Model>();
        }
        this.definitions.put(key, model);
    }

    public boolean hasDefinition(String key) {
        if (this.definitions == null) {
            return false;
        }
        return this.definitions.keySet().contains(key);
    }

    public Map<String, Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Parameter> parameters) {
        this.parameters = parameters;
    }

    public Parameter getParameter(String parameter) {
        if (this.parameters == null) {
            return null;
        }
        return this.parameters.get(parameter);
    }

    public void addParameter(String key, Parameter parameter) {
        if (this.parameters == null) {
            this.parameters = new LinkedHashMap<>();
        }
        this.parameters.put(key, parameter);
    }

    public Map<String, Response> getResponses() {
        return responses;
    }

    public void setResponses(Map<String, Response> responses) {
        this.responses = responses;
    }

    public ExternalDocs getExternalDocs() {
        return externalDocs;
    }

    public void setExternalDocs(ExternalDocs value) {
        externalDocs = value;
    }

    @JsonAnyGetter
    public Map<String, Object> getVendorExtensions() {
        return vendorExtensions;
    }

    @JsonAnySetter
    public void setVendorExtension(String name, Object value) {
        if (name.startsWith("x-")) {
            vendorExtension(name, value);
        }
    }

    public void parse() {
        LOGGER.debug("Spark-Swagger: Start parsing metadata");
        if (apiEndpoints != null) {
            apiEndpoints.forEach(endpoint -> {
                if (ignoreSpec == null || !ignoreSpec.ignored(endpoint.getEndpointDescriptor().getPath())) {

                    tag(endpoint.getEndpointDescriptor().getTag());
                    endpoint.getMethodDescriptors().forEach(methodDescriptor -> {

                        Operation op = new Operation();
                        op.tag(endpoint.getEndpointDescriptor().getTag().getName());
                        op.description(methodDescriptor.getDescription());

                        if (methodDescriptor.getSummary() != null) op.summary(methodDescriptor.getSummary());
                        if (methodDescriptor.getOperationId() != null) op.operationId(methodDescriptor.getOperationId());

                        List<Parameter> parameters = ParamsFactory.create(methodDescriptor.getPath(), methodDescriptor.getParameters());
                        op.setParameters(parameters);

                        // Supply Ignore configurations
                        DefinitionsFactory.ignoreSpec = this.ignoreSpec;

                        final ParameterDescriptor methodBody = methodDescriptor.getBody();
                        if (methodBody != null && methodBody.getModel() != null) {
                            final Model model = methodBody.getModel();
System.out.println("**ADDING DEF - method body model: " +model.getTitle());
                            addDefinition(model.getTitle(), model);
                            BodyParameter requestBody = new BodyParameter();
                            requestBody.name(methodBody.getName());
                            requestBody.description(methodBody.getDescription());
                            requestBody.setRequired(methodBody.isRequired());
System.out.println("**SETTING request body schema from method body model " +model.getTitle() +" " +model.getReference() +" " +model.getProperties() +" " +model);
                            requestBody.setSchema(model);
                            op.addParameter(requestBody);
                        }
                        else if (methodDescriptor.getRequestType() != null) {
                                // Process fields
                                Map<String, Model> definitions = DefinitionsFactory.create(methodDescriptor.getRequestType());
System.out.println("Body request type class " +methodDescriptor.getRequestType().getClass().getSimpleName() +" defs " +definitions);
                                for (String key : definitions.keySet()) {
                                    if (!hasDefinition(key)) {
System.out.println("ADDING DEF from request type " +methodDescriptor.getRequestType().getClass().getSimpleName() +" def " +key +" model: " +definitions.get(key).getTitle() +" "  +definitions.get(key).getProperties() +"  " +definitions.get(key));
//                                        addDefinition(key, definitions.get(key));
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

                            BodyParameter requestBody = new BodyParameter();
                            requestBody.name(methodBody != null? methodBody.getName() : "body");
                            requestBody.description(methodBody != null? methodBody.getDescription() : "Body object description");
                            requestBody.setRequired(methodBody != null? methodBody.isRequired() : true);
System.out.println("**SETTING body schema from model generated from request type " +methodDescriptor.getRequestType().getSimpleName() +" model " +model.getTitle() +" " +model.getReference() +" " +model.getProperties() +" " +model);
                            requestBody.setSchema(model);
                            op.addParameter(requestBody);
                        }

                        final Map<String, Response> responses = methodDescriptor.getResponses();
                        if (responses != null && !responses.isEmpty()) {

                            responses.forEach((code, response)->{

                                if (response.getResponseSchema() != null) {
                                    final Model resModel = response.getResponseSchema();
//System.out.println("**ADDING DEF Response " +code +" schema prop " +resSchema.getTitle() +" " +resSchema.getName() +" " +resSchema.getType() +" " +resSchema +" model: " +resModel.getTitle() +" " +resModel.getProperties() +" " +resModel);
System.out.println("**ADDING DEF Response " +code +" model: " +resModel.getTitle() +" " +resModel.getProperties() +" " +resModel);
                                    addDefinition(resModel.getTitle(), resModel);
                                    response.setSchema(new PropertyModelConverter().modelToProperty(resModel));

                                    if (resModel.getClassType() != null)
                                    {
                                        final Map<String, Model> definitions = DefinitionsFactory.create(resModel.getClassType());
                                        definitions.forEach((defKey, defModel)->{

                                            // If model title set, use it for the name in case it differs from the
                                            // actual class name.
                                            final String keyToUse = defKey.equals(resModel.getClassType().getSimpleName())?
                                                    (resModel.getTitle() != null? resModel.getTitle() : defKey) :
                                                    defKey;

                                            if (!hasDefinition(keyToUse)) {
                                                addDefinition(keyToUse, defModel);
                                            }
                                        });
                                    }
                                }
                                else if (response.getSchema() != null) {
                                    final Property resProp = response.getSchema();
                                    final Model resModel = new PropertyModelConverter().propertyToModel(resProp);
                                    addDefinition(resProp.getTitle(), resModel);
                                    response.setResponseSchema(resModel);
                                }
                                else if (response.getExamples() != null) {

                                    response.getExamples().forEach((exKey, example)->{

                                        final Map<String, Model> definitions = DefinitionsFactory.create(example.getClass());
System.out.println("Response example " +example +" class " +example.getClass() +" defs " +definitions);
                                        definitions.forEach((defKey, defModel)->{
                                            if (!hasDefinition(defKey)) {
System.out.println("**ADDING DEF Response example def " +defKey +" model " +defModel.getTitle() +" " +defModel.getProperties() +" " +defModel);
                                                addDefinition(defKey, defModel);
                                            }
                                        });

                                        final Property property;
                                        if (definitions.isEmpty()) {
System.out.println("Getting property from example class because class produced no models.");
                                            property = DefinitionsFactory.createProperty(null, example.getClass());
                                        } else {
System.out.println("Getting property from ref model that will be created from example class.");
                                            RefModel refModel = new RefModel();
                                            refModel.set$ref(example.getClass().getSimpleName());
                                            property = new PropertyModelConverter().modelToProperty(refModel);
                                        }
System.out.println("**SETTING response schema from response example property " +property.getTitle() +" " +property.getName() +" " +property.getType() +" " +property);
                                        property.setExample(example);
                                        response.setSchema(property);
                                        response.setResponseSchema(new PropertyModelConverter().propertyToModel(property));
                                    });
                                }
                                else if (methodDescriptor.getResponseType() != null) {
                                    final Model resTypeModel = typeToModel(methodDescriptor.getResponseType());
                                    response.setResponseSchema(resTypeModel);
                                }

                                op.addResponse(code, response);
                            });
                        }
                        else if (methodDescriptor.getResponseType() != null) {
                            final Property property = typeToProperty(methodDescriptor.getResponseType());

                            Response responseBody = new Response();
                            responseBody.description("successful operation");
                            responseBody.setSchema(property);
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

                        addOperation(methodDescriptor.getPath(), methodDescriptor.getMethod(), op);
                    });
                }
            });
            LOGGER.debug("Spark-Swagger: metadata successfully parsed");
        } else {
            LOGGER.debug("Spark-Swagger: No metadata to parse. Please check your SparkSwagger configurations and Endpoints Resolver");
        }
    }

    private Model typeToModel(final Class<?> type) {
        final Map<String, Model> definitions = DefinitionsFactory.create(type);
System.out.println("Type for model " +type.getSimpleName() +" defs " +definitions);
        for (final String key : definitions.keySet()) {
            if (!hasDefinition(key)) {
System.out.println("ADDING DEF from type " +type.getSimpleName() +" def " +key +" model: " +definitions.get(key).getTitle() +" "  +definitions.get(key).getProperties() +"  " +definitions.get(key));
//                addDefinition(key, definitions.get(key));
            }
        }

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
        final Map<String, Model> definitions = DefinitionsFactory.create(type);
System.out.println("Type for property " +type.getSimpleName() +" defs " +definitions);
        for (final String key : definitions.keySet()) {
            if (!hasDefinition(key)) {
System.out.println("**ADDING DEF from type " +type.getSimpleName() +" def " +key +" model: " +definitions.get(key).getTitle() +" "  +definitions.get(key).getProperties() +"  " +definitions.get(key));
                addDefinition(key, definitions.get(key));
            }
        }

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

    private void addOperation(String pathStr, HttpMethod method, Operation op) {
        String formattedPath = ParamsFactory.formatPath(pathStr);
        if (paths != null && paths.containsKey(formattedPath)) {
            Path path = paths.get(formattedPath);
            path.set(method, op);
        } else {
            Path path = new Path();
            path.set(method, op);
            path(formattedPath, path);
        }
        LOGGER.debug("Spark-Swagger: " + method.name() + " " + formattedPath + " parsed");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((basePath == null) ? 0 : basePath.hashCode());
        result = prime * result + ((consumes == null) ? 0 : consumes.hashCode());
        result = prime * result + ((definitions == null) ? 0 : definitions.hashCode());
        result = prime * result + ((externalDocs == null) ? 0 : externalDocs.hashCode());
        result = prime * result + ((host == null) ? 0 : host.hashCode());
        result = prime * result + ((info == null) ? 0 : info.hashCode());
        result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
        result = prime * result + ((paths == null) ? 0 : paths.hashCode());
        result = prime * result + ((produces == null) ? 0 : produces.hashCode());
        result = prime * result + ((responses == null) ? 0 : responses.hashCode());
        result = prime * result + ((schemes == null) ? 0 : schemes.hashCode());
        result = prime * result + ((security == null) ? 0 : security.hashCode());
        result = prime * result + ((securityDefinitions == null) ? 0 : securityDefinitions.hashCode());
        result = prime * result + ((swagger == null) ? 0 : swagger.hashCode());
        result = prime * result + ((tags == null) ? 0 : tags.hashCode());
        result = prime * result + ((vendorExtensions == null) ? 0 : vendorExtensions.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Swagger other = (Swagger) obj;
        if (basePath == null) {
            if (other.basePath != null) {
                return false;
            }
        } else if (!basePath.equals(other.basePath)) {
            return false;
        }
        if (consumes == null) {
            if (other.consumes != null) {
                return false;
            }
        } else if (!consumes.equals(other.consumes)) {
            return false;
        }
        if (definitions == null) {
            if (other.definitions != null) {
                return false;
            }
        } else if (!definitions.equals(other.definitions)) {
            return false;
        }
        if (externalDocs == null) {
            if (other.externalDocs != null) {
                return false;
            }
        } else if (!externalDocs.equals(other.externalDocs)) {
            return false;
        }
        if (host == null) {
            if (other.host != null) {
                return false;
            }
        } else if (!host.equals(other.host)) {
            return false;
        }
        if (info == null) {
            if (other.info != null) {
                return false;
            }
        } else if (!info.equals(other.info)) {
            return false;
        }
        if (parameters == null) {
            if (other.parameters != null) {
                return false;
            }
        } else if (!parameters.equals(other.parameters)) {
            return false;
        }
        if (paths == null) {
            if (other.paths != null) {
                return false;
            }
        } else if (!paths.equals(other.paths)) {
            return false;
        }
        if (produces == null) {
            if (other.produces != null) {
                return false;
            }
        } else if (!produces.equals(other.produces)) {
            return false;
        }
        if (responses == null) {
            if (other.responses != null) {
                return false;
            }
        } else if (!responses.equals(other.responses)) {
            return false;
        }
        if (schemes == null) {
            if (other.schemes != null) {
                return false;
            }
        } else if (!schemes.equals(other.schemes)) {
            return false;
        }
        if (security == null) {
            if (other.security != null) {
                return false;
            }
        } else if (!security.equals(other.security)) {
            return false;
        }
        if (securityDefinitions == null) {
            if (other.securityDefinitions != null) {
                return false;
            }
        } else if (!securityDefinitions.equals(other.securityDefinitions)) {
            return false;
        }
        if (swagger == null) {
            if (other.swagger != null) {
                return false;
            }
        } else if (!swagger.equals(other.swagger)) {
            return false;
        }
        if (tags == null) {
            if (other.tags != null) {
                return false;
            }
        } else if (!tags.equals(other.tags)) {
            return false;
        }
        if (vendorExtensions == null) {
            if (other.vendorExtensions != null) {
                return false;
            }
        } else if (!vendorExtensions.equals(other.vendorExtensions)) {
            return false;
        }
        return true;
    }

    public Swagger vendorExtensions(Map<String, Object> vendorExtensions) {
        if (vendorExtensions == null) {
            return this;
        }

        if (this.vendorExtensions == null) {
            this.vendorExtensions = new LinkedHashMap<>();
        }

        this.vendorExtensions.putAll(vendorExtensions);
        return this;
    }

    public void setVendorExtensions(Map<String, Object> vendorExtensions) {
        this.vendorExtensions = vendorExtensions;
    }
}
