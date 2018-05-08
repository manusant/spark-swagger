package com.coriant.sdn.ss.rest;

import spark.Request;
import spark.Response;

/**
 * @author manusant
 */
public interface TypedRoute<T, R> extends SparkRoute{

    R handleAndTransform(T body, Request request, Response response);
}