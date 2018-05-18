package com.beerboy.ss.rest;

import java.util.Collection;

/**
 * @author manusant
 */
@FunctionalInterface
public interface EndpointResolver {

    Collection<Endpoint> endpoints();
}