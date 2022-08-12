package io.github.manusant.ss.route;

import io.github.manusant.ss.annotation.Content;
import io.github.manusant.ss.conf.TypifyProvider;
import io.github.manusant.ss.exception.ReflectionExceptions;
import io.github.manusant.ss.model.ContentType;
import io.github.manusant.ss.rest.RestResponse;
import spark.Request;
import spark.Response;
import spark.Route;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;

import static io.github.manusant.ss.rest.RestResponse.notImplemented;
import static java.text.MessageFormat.format;

/**
 * @author manusant
 */
@SuppressWarnings({"unchecked"})
public abstract class TypedRoute<T> implements Route {

    public abstract Object onRequest(T body, Request request, Response response);

    @Override
    public Object handle(Request request, Response response) {
        try {
            Class<T> typeOfT = (Class<T>) ((ParameterizedType) getClass()
                    .getGenericSuperclass())
                    .getActualTypeArguments()[0];

            T requestObject = TypifyProvider.json().fromJson(request.body(), typeOfT);

            // Set content type on response to application/json
            Object result = onRequest(requestObject, request, response);

            Method method = getClass().getMethod("onRequest", Object.class, Request.class, Response.class);
            return RouteHelper.prepareResponse(response, result, method);
        } catch (NoSuchMethodException | SecurityException e) {
            ReflectionExceptions.handleReflectionException(e);
        }
        return null;
    }
}
