package io.github.manusant.ss.model;

import io.github.manusant.ss.model.properties.Property;

import java.util.Map;

public interface Model {
    String getTitle();

    void setTitle(String title);

    String getDescription();

    void setDescription(String description);

    Map<String, Property> getProperties();

    void setProperties(Map<String, Property> properties);

    Object getExample();

    void setExample(Object example);

    ExternalDocs getExternalDocs();

    String getReference();

    void setReference(String reference);

    Class<?> getTypeClass();

    void setTypeClass(Class<?> typeClass);

    Object clone();

    Map<String, Object> getVendorExtensions();
}
