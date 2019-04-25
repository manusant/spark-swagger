package io.github.manusant.ss.rest;

import io.github.manusant.ss.SparkSwagger;

/**
 * @author manusant
 */
@FunctionalInterface
public interface Endpoint {

    void bind(final SparkSwagger restApi);
}