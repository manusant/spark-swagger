package com.beerboy.ss.rest;

import spark.Request;
import spark.Response;
import spark.Route;

import java.lang.reflect.ParameterizedType;

/**
 * @author manusant
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class TypedGsonRoute<T, R> implements Route, TypedRoute<T, R> {

    @Override
    public Object handle(Request request, Response response) {

        Class<T> typeOfT = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];

        T requestObject = GsonProvider.gson().fromJson(request.body(), typeOfT);

        // Set content type on response to application/json
        R result = handleAndTransform(requestObject, request, response);
        if (result != null) {
            return GsonProvider.gson().toJson(result);
        }
        return null;
    }
}