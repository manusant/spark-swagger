package com.beerboy.ss.rest;

import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author manusant
 */
public abstract class GsonRoute implements Route, GenericRoute {

    @Override
    public Object handle(Request request, Response response) {

        // Set content type on response to application/json
        return GsonProvider.gson().toJson(handleAndTransform(request, response));
    }

    public static <T> T fromJson(String body, Class<T> type) {
        return GsonProvider.gson().fromJson(body, type);
    }
}