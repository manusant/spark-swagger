package com.coriant.sdn.ss.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Request;
import spark.Response;
import spark.Route;

import java.lang.reflect.ParameterizedType;

/**
 * @author manusant
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class TypedGsonRoute<T, R> implements Route, TypedRoute<T, R> {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public Object handle(Request request, Response response) {

        Class<T> typeOfT = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];

        T requestObject = GSON.fromJson(request.body(), typeOfT);

        // Set content type on response to application/json
        R result = handleAndTransform(requestObject, request, response);
        if (result != null) {
            return GSON.toJson(result);
        }
        return null;
    }
}