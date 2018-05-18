package com.beerboy.ss.rest;

import com.beerboy.ss.conf.IgnoreSpec;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Optional;

/**
 * @author manusant
 */
public class GsonProvider {

    private static Gson GSON;

    public static Gson create() {
        return create(null);
    }

    public static Gson create(IgnoreSpec ignoreSpec) {
        GSON = new GsonBuilder()
                .setPrettyPrinting()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                        return ignoreSpec != null && (ignoreSpec.ignoreAnnotated(fieldAttributes) || ignoreSpec.ignored(fieldAttributes));
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> aClass) {
                        return false;
                    }
                })
                .create();
        return GSON;
    }

    public static Gson gson() {
        Optional.ofNullable(GSON).orElseThrow(() -> new IllegalStateException("Please create a GSON instance through GsonProvider.create() and then use it :)"));
        return GSON;
    }
}
