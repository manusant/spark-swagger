package com.coriant.sdn.ss.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author manusant
 */
public abstract class GsonRoute implements Route, GenericRoute {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public Object handle(Request request, Response response) {

        // Set content type on response to application/json
        return GSON.toJson(handleAndTransform(request, response));
    }

    public static <T> T fromJson(String body, Class<T> type) {
        return GSON.fromJson(body, type);
    }
}