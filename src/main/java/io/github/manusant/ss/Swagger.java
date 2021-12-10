package io.github.manusant.ss;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.github.manusant.ss.conf.IgnoreSpec;
import io.github.manusant.ss.factory.DefinitionsFactory;
import io.github.manusant.ss.factory.ParamsFactory;
import io.github.manusant.ss.model.*;
import io.github.manusant.ss.model.auth.SecuritySchemeDefinition;
import io.github.manusant.ss.model.parameters.Parameter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author manusant
 */
@JsonInclude(Include.NON_NULL)
public class Swagger {

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

    public Swagger ignores(IgnoreSpec ignoreSpec) {
        this.ignoreSpec = ignoreSpec;
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

    public IgnoreSpec getIgnoreSpec() {
        return ignoreSpec;
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

    protected Map<String, Model> addDefinitionsForType(final String typeName, final Class<?> type) {
        final Map<String, Model> definitions = DefinitionsFactory.create(type);
        definitions.forEach((defKey, defModel) -> {

            // If model title set, use it for the name in case it differs from the
            // actual class name. This allows the documentation to have different
            // names for the models, that might be more useful for the user than the
            // actual names in the code.
            final String keyToUse = defKey.equals(type.getSimpleName()) ?
                    (typeName != null ? typeName : defKey) :
                    defKey;

            if (!hasDefinition(keyToUse)) {
                addDefinition(keyToUse, defModel);
            }
        });
        return definitions;
    }

    protected void addOperation(String pathStr, HttpMethod method, Operation op) {
        String formattedPath = ParamsFactory.formatPath(pathStr);
        if (paths != null && paths.containsKey(formattedPath)) {
            Path path = paths.get(formattedPath);
            path.set(method, op);
        } else {
            Path path = new Path();
            path.set(method, op);
            path(formattedPath, path);
        }
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
