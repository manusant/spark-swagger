package com.beerboy.ss.rest;

import spark.Request;
import spark.Response;

/**
 * @author manusant
 */
public interface GenericRoute extends SparkRoute{

    Object handleAndTransform(Request request, Response response);
}