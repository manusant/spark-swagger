package io.github.manusant.ss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonInclude(JsonInclude.Include.NON_NULL)
public enum Scheme {
    HTTP("http"),
    HTTPS("https"),
    WS("ws"),
    WSS("wss");

    private final String value;

    private Scheme(String value) {
        this.value = value;
    }

    @JsonCreator
    public static Scheme forValue(String value) {
        for (Scheme item : Scheme.values()) {
            if (item.name().equalsIgnoreCase(value) || item.toValue().equalsIgnoreCase(value)) {
                return item;
            }
        }
        return null;
    }

    @JsonValue
    public String toValue() {
        return value;
    }
}