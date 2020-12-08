package io.github.manusant.ss.descriptor;

import io.github.manusant.ss.model.ExternalDocs;
import io.github.manusant.ss.model.HttpMethod;
import io.github.manusant.ss.model.Response;
import io.github.manusant.ss.rest.RestResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author manusant
 */
public class MethodDescriptor {

    private HttpMethod method;
    private String path;
    private String summary;
    private String description;
    private Class<?> requestType;
    private boolean requestAsCollection;
    private Class<?> responseType;
    private boolean responseAsCollection;
    private String operationId;
    private List<String> consumes;
    private List<String> produces;
    private List<ParameterDescriptor> parameters = new ArrayList<>();
    private Map<String, Response> responses;
    private ExternalDocs externalDocs;
    private Boolean deprecated;

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Class<?> getRequestType() {
        return requestType;
    }

    public void setRequestType(Class<?> requestType) {
        this.requestType = requestType;
    }

    public boolean isRequestAsCollection() {
        return requestAsCollection;
    }

    public void setRequestAsCollection(boolean requestAsCollection) {
        this.requestAsCollection = requestAsCollection;
    }

    public Class<?> getResponseType() {
        return responseType;
    }

    public void setResponseType(Class<?> responseType) {
        this.responseType = responseType;
    }

    public boolean isResponseAsCollection() {
        return responseAsCollection;
    }

    public void setResponseAsCollection(boolean responseAsCollection) {
        this.responseAsCollection = responseAsCollection;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public List<String> getConsumes() {
        return consumes;
    }

    public void setConsumes(List<String> consumes) {
        this.consumes = consumes;
    }

    public List<String> getProduces() {
        return produces;
    }

    public void setProduces(List<String> produces) {
        this.produces = produces;
    }

    public List<ParameterDescriptor> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterDescriptor> parameters) {
        this.parameters = parameters;
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

    public void setExternalDocs(ExternalDocs externalDocs) {
        this.externalDocs = externalDocs;
    }

    public Boolean getDeprecated() {
        return deprecated;
    }

    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
    }

    public static Builder path(String path) {
        return new Builder().withPath(path);
    }

    public static final class Builder {
        private HttpMethod method;
        private String path;
        private String summary;
        private String description;
        private Class requestType;
        private boolean requestAsCollection;
        private Class responseType;
        private boolean responseAsCollection;
        private String operationId;
        private List<String> consumes;
        private List<String> produces;
        private List<ParameterDescriptor> parameters = new ArrayList<>();
        private Map<String, Response> responses;
        private ExternalDocs externalDocs;
        private Boolean deprecated;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder withMethod(HttpMethod method) {
            this.method = method;
            return this;
        }

        public Builder withPath(String path) {
            this.path = path;
            return this;
        }

        public Builder withSummary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withRequestType(Class requestType) {
            this.requestType = requestType;
            return this;
        }

        public Builder withRequestAsCollection(Class itemType) {
            this.requestAsCollection = true;
            this.requestType = itemType;
            return this;
        }

        public Builder withResponseType(Class responseType) {
            this.responseType = responseType;
            return this;
        }

        public Builder withGenericResponse() {
            this.responseType = RestResponse.class;
            return this;
        }

        public Builder withResponseAsCollection(Class itemType) {
            this.responseAsCollection = true;
            this.responseType = itemType;
            return this;
        }

        public Builder withOperationId(String operationId) {
            this.operationId = operationId;
            return this;
        }

        public Builder withConsumes(List<String> consumes) {
            this.consumes = consumes;
            return this;
        }

        public Builder withProduces(List<String> produces) {
            this.produces = produces;
            return this;
        }

        public Builder withParams(List<ParameterDescriptor> parameters) {
            this.parameters = parameters;
            return this;
        }

        public Builder withParam(ParameterDescriptor parameter) {
            this.parameters.add(parameter);
            return this;
        }

        public ParameterDescriptor.Builder withPathParam() {
            return ParameterDescriptor.newBuilder(this).withType(ParameterDescriptor.ParameterType.PATH);
        }

        public Builder withPathParam(ParameterDescriptor param) {
            param.setType(ParameterDescriptor.ParameterType.PATH);
            this.parameters.add(param);
            return this;
        }

        public ParameterDescriptor.Builder withQueryParam() {
            return ParameterDescriptor.newBuilder(this).withType(ParameterDescriptor.ParameterType.QUERY);
        }

        public Builder withQueryParam(ParameterDescriptor param) {
            param.setType(ParameterDescriptor.ParameterType.QUERY);
            this.parameters.add(param);
            return this;
        }

        public Builder withResponses(Map<String, Response> responses) {
            this.responses = responses;
            return this;
        }

        public Builder withExternalDocs(ExternalDocs externalDocs) {
            this.externalDocs = externalDocs;
            return this;
        }

        public Builder withDeprecated(Boolean deprecated) {
            this.deprecated = deprecated;
            return this;
        }

        public MethodDescriptor build() {
            MethodDescriptor methodDescriptor = new MethodDescriptor();
            methodDescriptor.setMethod(method);
            methodDescriptor.setPath(path);
            methodDescriptor.setSummary(summary);
            methodDescriptor.setDescription(description);
            methodDescriptor.setRequestType(requestType);
            methodDescriptor.setRequestAsCollection(requestAsCollection);
            methodDescriptor.setResponseType(responseType);
            methodDescriptor.setResponseAsCollection(responseAsCollection);
            methodDescriptor.setOperationId(operationId);
            methodDescriptor.setConsumes(consumes);
            methodDescriptor.setProduces(produces);
            methodDescriptor.setParameters(parameters);
            methodDescriptor.setResponses(responses);
            methodDescriptor.setExternalDocs(externalDocs);
            methodDescriptor.setDeprecated(deprecated);
            return methodDescriptor;
        }
    }
}
