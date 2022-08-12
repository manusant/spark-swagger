package io.github.manusant.ss.route;

import io.github.manusant.ss.annotation.Content;
import io.github.manusant.ss.conf.TypifyProvider;
import io.github.manusant.ss.exception.ReflectionExceptions;
import io.github.manusant.ss.model.ContentType;
import spark.Request;
import spark.Response;

import java.lang.reflect.Method;

import static io.github.manusant.ss.rest.RestResponse.notImplemented;
import static java.text.MessageFormat.format;

/**
 * @author manusant
 */
public abstract class Route implements spark.Route {

    public abstract Object onRequest(Request request, Response response);

    @Override
    public Object handle(Request request, Response response) {
        Object result = onRequest(request, response);
        try {
            if (result != null) {
                Method method = getClass().getMethod("onRequest", Request.class, Response.class);
                return RouteHelper.prepareResponse(response, result, method);
            }
        } catch (NoSuchMethodException | SecurityException e) {
            ReflectionExceptions.handleReflectionException(e);
        }
        return null;
    }


}
