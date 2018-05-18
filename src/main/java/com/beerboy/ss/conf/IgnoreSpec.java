package com.beerboy.ss.conf;

import com.google.gson.FieldAttributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author manusant
 */
public class IgnoreSpec {

    private List<Class<? extends Annotation>> ignoreAnnotated;
    private List<Class<?>> ignoreTypes;
    private List<String> endpointPaths;

    public List<Class<? extends Annotation>> getIgnoreAnnotated() {
        return ignoreAnnotated;
    }

    public void setIgnoreAnnotated(List<Class<? extends Annotation>> ignoreAnnotated) {
        this.ignoreAnnotated = ignoreAnnotated;
    }

    public List<Class<?>> getIgnoreTypes() {
        return ignoreTypes;
    }

    public void setIgnoreTypes(List<Class<?>> ignoreTypes) {
        this.ignoreTypes = ignoreTypes;
    }

    public List<String> getEndpointPaths() {
        return endpointPaths;
    }

    public void setEndpointPaths(List<String> endpointPaths) {
        this.endpointPaths = endpointPaths;
    }

    public boolean ignored(String path) {
        return this.endpointPaths != null && this.endpointPaths.contains(path);
    }

    public boolean ignored(Field field) {
        if (this.ignoreTypes != null) {
            if (field.getType().isArray() || Collection.class.isAssignableFrom(field.getType())) {
                Type type = field.getGenericType();
                if (type instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType) type;
                    for (Type paramType : pt.getActualTypeArguments()) {
                        if (this.ignoreTypes.stream().map(Class::getTypeName).anyMatch(typeName -> typeName.equalsIgnoreCase(paramType.getTypeName()))) {
                            return true;
                        }
                    }
                }
            }
            return this.ignoreTypes.contains(field.getType());
        }
        return false;
    }

    public boolean ignored(FieldAttributes field) {
        if (this.ignoreTypes != null) {
            if (field.getDeclaredClass().isArray() || Collection.class.isAssignableFrom(field.getDeclaredClass())) {
                Type type = field.getDeclaredType();
                if (type instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType) type;
                    for (Type paramType : pt.getActualTypeArguments()) {
                        if (this.ignoreTypes.stream().map(Class::getTypeName).anyMatch(typeName -> typeName.equalsIgnoreCase(paramType.getTypeName()))) {
                            return true;
                        }
                    }
                }
            }
            return this.ignoreTypes.contains(field.getDeclaredClass());
        }
        return false;
    }

    public boolean ignoreAnnotated(Class<?> type) {
        if (this.ignoreAnnotated != null) {
            for (Class<? extends Annotation> annotationType : this.ignoreAnnotated) {
                if (type.isAnnotationPresent(annotationType)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean ignoreAnnotated(Field field) {
        if (this.ignoreAnnotated != null) {
            for (Class<? extends Annotation> annotationType : this.ignoreAnnotated) {
                if (field.getAnnotation(annotationType) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean ignoreAnnotated(FieldAttributes field) {
        if (this.ignoreAnnotated != null) {
            for (Class<? extends Annotation> annotationType : this.ignoreAnnotated) {
                if (field.getAnnotation(annotationType) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private List<Class<? extends Annotation>> ignoreAnnotated;
        private List<Class<?>> ignoreTypes;
        private List<String> endpointPaths;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder withIgnoreAnnotated(Class<? extends Annotation>... ignoreAnnotated) {
            this.ignoreAnnotated = Arrays.asList(ignoreAnnotated);
            return this;
        }

        public Builder withIgnoreTypes(Class<?>... ignoreTypes) {
            this.ignoreTypes = Arrays.asList(ignoreTypes);
            return this;
        }

        public Builder withEndpointPaths(String... endpointPaths) {
            this.endpointPaths = Arrays.asList(endpointPaths);
            return this;
        }

        public IgnoreSpec build() {
            IgnoreSpec ignoreSpec = new IgnoreSpec();
            ignoreSpec.setIgnoreAnnotated(ignoreAnnotated);
            ignoreSpec.setIgnoreTypes(ignoreTypes);
            ignoreSpec.setEndpointPaths(endpointPaths);
            return ignoreSpec;
        }
    }
}
