package com.beerboy.ss.factory;

import com.beerboy.ss.descriptor.ParameterDescriptor;
import com.beerboy.ss.model.parameters.AbstractSerializableParameter;
import com.beerboy.ss.model.parameters.Parameter;
import com.beerboy.ss.model.parameters.PathParameter;
import com.beerboy.ss.model.parameters.QueryParameter;
import com.beerboy.ss.model.properties.*;
import spark.utils.SparkUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author manusant
 */
public class ParamsFactory {

    public static List<Parameter> create(final String pathUri, final List<ParameterDescriptor> parameterDescriptors) {
        List<Parameter> parameters = new ArrayList<>();

        List<Parameter> pathParams = createPathParams(pathUri, parameterDescriptors);
        parameters.addAll(pathParams);
        List<Parameter> queryParams = createQueryParams(parameterDescriptors);
        parameters.addAll(queryParams);
        return parameters;
    }

    private static List<Parameter> createPathParams(String pathUri, List<ParameterDescriptor> parameterDescriptors) {
        List<Parameter> parameters = new ArrayList<>();

        for (String uriPart : SparkUtils.convertRouteToList(pathUri)) {
            if (SparkUtils.isParam(uriPart)) {
                try {
                    final String param = URLDecoder.decode(uriPart, "UTF-8");
                    final String decodedParam = param.contains(":") ? param.substring(param.indexOf(":") + 1) : param;

                    Optional<ParameterDescriptor> foundParam = parameterDescriptors != null ? parameterDescriptors.stream()
                            .filter(parameterDescriptor -> parameterDescriptor.getName().equals(decodedParam))
                            .findFirst() : Optional.empty();

                    if (foundParam.isPresent()) {
                        // Parameter Descriptor provided
                        ParameterDescriptor parameterDescriptor = foundParam.get();
                        if (parameterDescriptor.getType() == ParameterDescriptor.ParameterType.PATH) {
                            parameters.add(toPath(parameterDescriptor));
                        }
                    } else {
                        // Generic description for parameter
                        parameters.add(toGeneric(decodedParam));
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        return parameters;
    }

    private static List<Parameter> createQueryParams(List<ParameterDescriptor> parameterDescriptors) {
        return filter(parameterDescriptors, ParameterDescriptor.ParameterType.QUERY)
                .stream()
                .map(ParamsFactory::toQuey)
                .collect(Collectors.toList());
    }

    public static String formatPath(String pathUri) {
        StringBuilder formatted = new StringBuilder();

        List<String> uriParts = SparkUtils.convertRouteToList(pathUri);
        for (String uriPart : uriParts) {
            try {
                formatted.append("/");
                if (SparkUtils.isParam(uriPart)) {
                    final String param = URLDecoder.decode(uriPart, "UTF-8");
                    final String decodedParam = param.contains(":") ? param.substring(param.indexOf(":") + 1) : param;
                    formatted.append("{").append(decodedParam).append("}");
                } else {
                    formatted.append(uriPart);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        String path = formatted.toString();
        return path.isEmpty() ? "/" : path;
    }

    private static PathParameter toGeneric(String param) {
        PathParameter parameter = new PathParameter();
        parameter.setName(param);
        parameter.setRequired(true);
        parameter.setProperty(createProperty(String.class));
        return parameter;
    }

    private static PathParameter toPath(final ParameterDescriptor parameterDescriptor) {
        PathParameter parameter = new PathParameter();
        bindAttributes(parameterDescriptor, parameter);
        parameter.setRequired(true);
        return parameter;
    }

    private static QueryParameter toQuey(final ParameterDescriptor parameterDescriptor) {
        QueryParameter parameter = new QueryParameter();
        bindAttributes(parameterDescriptor, parameter);
        return parameter;
    }

    private static void bindAttributes(ParameterDescriptor parameterDescriptor, AbstractSerializableParameter parameter) {
        parameter.setName(parameterDescriptor.getName());
        parameter.setDescription(parameterDescriptor.getDescription());
        parameter.setPattern(parameterDescriptor.getPattern());
        parameter.setExample(parameterDescriptor.getExample());
        parameter.setAllowEmptyValue(parameterDescriptor.getAllowEmptyValue());
        parameter.setDefaultValue(parameterDescriptor.getDefaultValue());
        if (parameterDescriptor.getObject() != null) {
            parameter.setProperty(createProperty(parameterDescriptor.getObject()));
        } else if (parameterDescriptor.getCollectionOf() != null) {
            Property itemProperty = createProperty(parameterDescriptor.getCollectionOf());
            ArrayProperty collectionProperty = new ArrayProperty();
            collectionProperty.setItems(itemProperty);
            parameter.setProperty(collectionProperty);
        } else {
            parameter.setProperty(createProperty(String.class));
        }
    }

    private static Property createProperty(Class type) {
        if (type.isEnum()) {
            StringProperty property = new StringProperty();
            property._enum(Stream.of(type.getEnumConstants()).map(o -> ((Enum) o).name()).collect(Collectors.toList()));
            return property;
        } else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            return new BooleanProperty();
        } else if (type == byte[].class) {
            return new ByteArrayProperty();
        } else if (type.equals(Number.class)) {
            return new DecimalProperty();
        } else if (type.equals(Double.class) || type.equals(double.class)) {
            return new DoubleProperty();
        } else if (type.equals(Float.class) || type.equals(float.class)) {
            return new FloatProperty();
        } else if (type.equals(Integer.class) || type.equals(int.class)) {
            return new IntegerProperty();
        } else if (type.equals(Long.class) || type.equals(long.class)) {
            return new LongProperty();
        } else if (type.equals(String.class)) {
            return new StringProperty();
        } else if (type.equals(UUID.class)) {
            return new UUIDProperty();
        }
        throw new UnsupportedOperationException("Only 'String' or 'primitive' types can be used as parameters");
    }

    private static List<ParameterDescriptor> filter(List<ParameterDescriptor> params, ParameterDescriptor.ParameterType type) {
        return params.stream().filter(parameterDescriptor -> parameterDescriptor.getType() == type).collect(Collectors.toList());
    }
}
