package com.beerboy.ss.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by russellb337 on 7/10/15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public enum HttpMethod {
    POST,
    GET,
    PUT,
    PATCH,
    DELETE,
    HEAD,
    OPTIONS
}
