package com.beerboy.ss.rest;

import com.beerboy.ss.SparkSwagger;

/**
 * @author manusant
 */
@FunctionalInterface
public interface Endpoint {

    void bind(final SparkSwagger restApi);
}