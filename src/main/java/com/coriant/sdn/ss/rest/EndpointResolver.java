package com.coriant.sdn.ss.rest;

import java.util.Set;

/**
 * @author manusant
 */
@FunctionalInterface
public interface EndpointResolver {

    Set<Endpoint> modules();
}