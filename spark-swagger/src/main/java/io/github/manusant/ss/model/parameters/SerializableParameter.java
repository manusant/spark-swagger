package io.github.manusant.ss.model.parameters;

import io.github.manusant.ss.model.properties.Property;

import java.math.BigDecimal;
import java.util.List;

public interface SerializableParameter extends Parameter {
    String getType();

    void setType(String type);

    Property getItems();

    void setItems(Property items);

    String getFormat();

    void setFormat(String format);

    String getCollectionFormat();

    void setCollectionFormat(String collectionFormat);

    List<String> getEnum();

    void setEnum(List<String> _enum);

    List<Object> getEnumValue();

    void setEnumValue(List<?> enumValue);

    Integer getMaxLength();

    void setMaxLength(Integer maxLength);

    Integer getMinLength();

    void setMinLength(Integer minLength);

    String getPattern();

    void setPattern(String pattern);

    Boolean isUniqueItems();

    void setUniqueItems(Boolean uniqueItems);

    Number getMultipleOf();

    void setMultipleOf(Number multipleOf);

    Boolean isExclusiveMaximum();

    void setExclusiveMaximum(Boolean exclusiveMinimum);

    Boolean isExclusiveMinimum();

    void setExclusiveMinimum(Boolean exclusiveMinimum);

    BigDecimal getMaximum();

    void setMaximum(BigDecimal maximum);

    BigDecimal getMinimum();

    void setMinimum(BigDecimal minimum);

    Integer getMaxItems();

    void setMaxItems(Integer maxItems);

    Integer getMinItems();

    void setMinItems(Integer minItems);

    Boolean getAllowEmptyValue();

    void setAllowEmptyValue(Boolean allowEmptyValue);

}
