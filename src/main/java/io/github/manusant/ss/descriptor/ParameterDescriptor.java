package io.github.manusant.ss.descriptor;

import java.util.Optional;

import com.fasterxml.jackson.databind.node.ObjectNode;

import io.github.manusant.ss.model.Model;

/**
 * @author manusant
 */
public class ParameterDescriptor {
    public enum ParameterType {
        PATH,
        QUERY
    }

    private String name;
    private String pattern;
    private ParameterType type;
    private String description;
    private boolean required = false;
    private String example;
    private ObjectNode exampleJson;
    private Boolean allowEmptyValue;
    private Model model;
    private Class object;
    private Class collectionOf;
    private String defaultValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public ParameterType getType() {
        return type;
    }

    public void setType(ParameterType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public ObjectNode getExampleJson() {
        return exampleJson;
    }
    public ParameterDescriptor setExampleJson(ObjectNode exampleJson) {
        this.exampleJson = exampleJson;
        return this;
    }

    public Boolean getAllowEmptyValue() {
        return allowEmptyValue;
    }

    public void setAllowEmptyValue(Boolean allowEmptyValue) {
        this.allowEmptyValue = allowEmptyValue;
    }


    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Class getObject() {
        return object;
    }

    public void setObject(Class object) {
        this.object = object;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Class getCollectionOf() {
        return collectionOf;
    }

    public void setCollectionOf(Class collectionOf) {
        this.collectionOf = collectionOf;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(final MethodDescriptor.Builder methodDescriptor) {
        return new Builder(methodDescriptor);
    }

    public static final class Builder {
        private String name;
        private String pattern;
        private ParameterType type;
        private String description;
        private boolean required = false;
        private String example;
        private Boolean allowEmptyValue;
        private Model model;
        private Class object;
        private Class collectionOf;
        private String defaultValue;
        private MethodDescriptor.Builder methodDescriptor;

        private Builder() {
        }

        private Builder(final MethodDescriptor.Builder methodDescriptor) {
            this.methodDescriptor = methodDescriptor;
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withPattern(String pattern) {
            this.pattern = pattern;
            return this;
        }

        public Builder withType(ParameterType type) {
            this.type = type;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withRequired(boolean required) {
            this.required = required;
            return this;
        }

        public Builder withExample(String example) {
            this.example = example;
            return this;
        }

        public Builder withAllowEmptyValue(Boolean allowEmptyValue) {
            this.allowEmptyValue = allowEmptyValue;
            return this;
        }

        public Builder withModel(Model model) {
            this.model = model;
            return this;
        }

        public Builder withObject(Class object) {
            this.object = object;
            return this;
        }

        public Builder withCollectionOf(Class collectionOf) {
            this.collectionOf = collectionOf;
            return this;
        }

        public Builder withDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public ParameterDescriptor build() {
            ParameterDescriptor parameterDescriptor = new ParameterDescriptor();
            parameterDescriptor.setName(name);
            parameterDescriptor.setPattern(pattern);
            parameterDescriptor.setType(type);
            parameterDescriptor.setDescription(description);
            parameterDescriptor.setRequired(required);
            parameterDescriptor.setExample(example);
            parameterDescriptor.setAllowEmptyValue(allowEmptyValue);
            parameterDescriptor.setModel(model);
            parameterDescriptor.setObject(object);
            parameterDescriptor.setCollectionOf(collectionOf);
            parameterDescriptor.setDefaultValue(defaultValue);
            return parameterDescriptor;
        }

        public MethodDescriptor.Builder and() {
            Optional.ofNullable(methodDescriptor).orElseThrow(() -> new IllegalStateException("AND operation is only available if building from a MethodDescriptor"));
            ParameterDescriptor parameter = build();
            methodDescriptor.withParam(parameter);
            return methodDescriptor;
        }
    }
}
