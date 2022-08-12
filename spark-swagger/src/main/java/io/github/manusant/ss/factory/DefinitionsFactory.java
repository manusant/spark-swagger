package io.github.manusant.ss.factory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.github.manusant.ss.conf.IgnoreSpec;
import io.github.manusant.ss.model.Model;
import io.github.manusant.ss.model.ModelImpl;
import io.github.manusant.ss.model.properties.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author manusant
 */
public class DefinitionsFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefinitionsFactory.class);

    public static IgnoreSpec ignoreSpec;

    public static Map<String, Model> create(Class type) {
        Map<String, Model> definitions = new HashMap<>();

        if (isObject(type)) {
            ModelImpl model = new ModelImpl();
            model.setType(ModelImpl.OBJECT);
            definitions.put(type.getSimpleName(), model);

            Map<String, Model> refDefinitions = parseProperties(model, type.getDeclaredFields());
            definitions.putAll(refDefinitions);
        }
        return definitions;
    }

    private static Map<String, Model> parseProperties(ModelImpl model, Field[] fields) {
        Map<String, Model> refDefinitions = new HashMap<>();

        for (Field field : fields) {
            if (DefinitionsFactory.ignoreSpec == null || !(DefinitionsFactory.ignoreSpec.ignored(field) || DefinitionsFactory.ignoreSpec.ignoreAnnotated(field))) {
                if (isViable(field)) {
                    Property property = createProperty(field, field.getType());
                    model.addProperty(field.getName(), property);

                    if (isRef(field.getType())) {
                        Map<String, Model> definitions = create(field.getType());
                        refDefinitions.putAll(definitions);
                    } else if (field.getType().isArray() || Collection.class.isAssignableFrom(field.getType())) {
                        Class<?> childType = getCollectionType(field);
                        if (isRef(childType)) {
                            Map<String, Model> definitions = create(childType);
                            refDefinitions.putAll(definitions);
                        }
                    }
                }
            }
        }
        return refDefinitions;
    }

    private static boolean isViable(Field field) {
        return !Modifier.isStatic(field.getModifiers());
    }

    public static Property createProperty(Field field, Class<?> fieldClass) {
        if (fieldClass.isEnum()) {
            StringProperty property = new StringProperty();
            property._enum(Stream.of(fieldClass.getEnumConstants()).map(o -> ((Enum) o).name()).collect(Collectors.toList()));
            return property;
        } else if (fieldClass.equals(boolean.class) || fieldClass.equals(Boolean.class)) {
            return new BooleanProperty();
        } else if (fieldClass == byte[].class) {
            return new ByteArrayProperty();
        } else if (fieldClass.equals(Date.class) || fieldClass.equals(java.sql.Date.class)) {
            return new DateProperty();
        } else if (fieldClass.equals(Number.class)) {
            return new DecimalProperty();
        } else if (fieldClass.equals(Double.class) || fieldClass.equals(double.class)) {
            return new DoubleProperty();
        } else if (fieldClass.equals(Float.class) || fieldClass.equals(float.class)) {
            return new FloatProperty();
        } else if (fieldClass.equals(Integer.class) || fieldClass.equals(int.class)) {
            return new IntegerProperty();
        } else if (fieldClass.equals(Long.class) || fieldClass.equals(long.class)) {
            return new LongProperty();
        } else if (fieldClass.equals(String.class)) {
            return new StringProperty();
        } else if (fieldClass.equals(UUID.class)) {
            return new UUIDProperty();
        } else if (fieldClass.isArray() || Collection.class.isAssignableFrom(fieldClass)) {
            ArrayProperty property = new ArrayProperty();
            // FIXME set actual items
            property.setItems(getCollectionProperty(field));
            return property;
        } else if (File.class.isAssignableFrom(fieldClass)) {
            return new FileProperty();
        } else {
            RefProperty property = new RefProperty();
            property.set$ref("#/definitions/" + fieldClass.getSimpleName());
            return property;
        }
    }

    public static Property createProperty(final String name, final JsonNode node) {
        switch (node.getNodeType()) {
            case BOOLEAN: {
                final BooleanProperty prop = new BooleanProperty();
                if (name != null) prop.setName(name);
                return prop;
            }
            case NUMBER: {
                final IntegerProperty prop = new IntegerProperty();
                if (name != null) prop.setName(name);
                return prop;
            }
            case STRING: {
                final StringProperty prop = new StringProperty();
                if (name != null) prop.setName(name);
                return prop;
            }
            case ARRAY: {
                final ArrayProperty arrayProp = new ArrayProperty();
                if (name != null) arrayProp.setName(name);
                final Iterator<JsonNode> elements = ((ArrayNode) node).iterator();
                Property elementProp = null;
                while (elements.hasNext()) {
                    if (elementProp == null) {
                        elementProp = DefinitionsFactory.createProperty(null, elements.next());
                        continue;
                    }
                    if (!(elementProp instanceof ObjectProperty)) break;
                    final Property nextElementProp = DefinitionsFactory.createProperty(null, elements.next());
                    if (!(nextElementProp instanceof ObjectProperty)) break;
                    final ObjectProperty nextObjProp = (ObjectProperty) nextElementProp;
                    final ObjectProperty objProp = (ObjectProperty) elementProp;
                    nextObjProp.getProperties().forEach((fieldName, fieldProp) -> {
                        objProp.property(fieldName, fieldProp);
                    });
                }
                arrayProp.setItems(elementProp);
                return arrayProp;
            }
            case OBJECT: {
                final ObjectProperty objProp = new ObjectProperty();
                if (name != null) objProp.setName(name);
                final Iterator<Map.Entry<String, JsonNode>> objFields = node.fields();
                while (objFields.hasNext()) {
                    final Map.Entry<String, JsonNode> field = objFields.next();
                    final String fieldName = field.getKey().trim();
                    if (fieldName == null || fieldName.isEmpty()) continue;
                    final Property fieldProp = DefinitionsFactory.createProperty(fieldName, field.getValue());
                    objProp.property(fieldName, fieldProp);
                }
                return objProp;
            }
            case BINARY:
            case MISSING:
            case NULL:
            case POJO:
        }
        return null;
    }

    private static boolean isRef(Class<?> fieldClass) {
        if (fieldClass.isEnum()
                || fieldClass.equals(boolean.class)
                || fieldClass.equals(Boolean.class)
                || fieldClass == byte[].class
                || fieldClass.equals(Date.class)
                || fieldClass.equals(java.sql.Date.class)
                || fieldClass.equals(Number.class)
                || fieldClass.equals(Double.class)
                || fieldClass.equals(double.class)
                || fieldClass.equals(Float.class)
                || fieldClass.equals(float.class)
                || fieldClass.equals(Integer.class)
                || fieldClass.equals(int.class)
                || fieldClass.equals(Long.class)
                || fieldClass.equals(long.class)
                || fieldClass.equals(String.class)
                || fieldClass.equals(UUID.class)
                || fieldClass.isArray()
                || fieldClass.equals(Object.class)
                || Collection.class.isAssignableFrom(fieldClass)
                || File.class.isAssignableFrom(fieldClass)
                || fieldClass.getCanonicalName().contains("java")
        ) {
            return false;
        } else {
            return true;
        }
    }

    private static boolean isObject(Class<?> fieldClass) {
        if (fieldClass.isEnum()
                || fieldClass.equals(boolean.class)
                || fieldClass.equals(Boolean.class)
                || fieldClass == byte[].class
                || fieldClass.equals(Number.class)
                || fieldClass.equals(Double.class)
                || fieldClass.equals(double.class)
                || fieldClass.equals(Float.class)
                || fieldClass.equals(float.class)
                || fieldClass.equals(Integer.class)
                || fieldClass.equals(int.class)
                || fieldClass.equals(Long.class)
                || fieldClass.equals(long.class)
                || fieldClass.equals(String.class)
        ) {
            return false;
        } else {
            return true;
        }
    }

    private static Property getCollectionProperty(Field collectionField) {
        Class<?> childType = getCollectionType(collectionField);
        return createProperty(collectionField, childType);
    }

    private static Class<?> getCollectionType(Field collectionField) {
        try {
            ParameterizedType parameterizedType = (ParameterizedType) collectionField.getGenericType();

            Type actualType = parameterizedType.getActualTypeArguments()[0];
            if (actualType instanceof Class) {
                return (Class<?>) actualType;
            } else if (actualType instanceof ParameterizedType) {
                return (Class<?>) ((ParameterizedType) actualType).getActualTypeArguments()[0];
            }
        } catch (ClassCastException e) {
            LOGGER.error("Field mapping not supported. ", e);
        }
        // FIXME resolve actual type in strange collection types
        return Object.class;
    }
}
