package com.coriant.sdn.ss.rest;

import com.coriant.sdn.ss.SparkSwagger;

/**
 * @author manusant
 */
@FunctionalInterface
public interface Endpoint {

    void bind(final SparkSwagger restApi);
}