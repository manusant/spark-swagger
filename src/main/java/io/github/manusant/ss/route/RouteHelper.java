package io.github.manusant.ss.route;

import io.github.manusant.ss.annotation.Content;
import io.github.manusant.ss.conf.TypifyProvider;
import io.github.manusant.ss.model.ContentType;
import spark.Response;

import java.lang.reflect.Method;

import static io.github.manusant.ss.rest.RestResponse.notImplemented;
import static java.text.MessageFormat.format;

public class RouteHelper {

    public static Object prepareResponse(Response response, Object result, Method method) {
        Content content = method.getAnnotation(Content.class);
        ContentType contentType = content != null ? content.value():ContentType.APPLICATION_JSON;
        response.type(contentType.getValue());

        if (contentType == ContentType.APPLICATION_JSON) {
            return TypifyProvider.json().toJson(result);
        } else {
            return notImplemented(response, format("'{0}' not implemented yet", contentType.getValue()));
        }
    }
}
