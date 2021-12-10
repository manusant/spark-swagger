package io.github.manusant.ss.conf;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.function.Supplier;

/**
 * @author manusant
 */
public class TypifyProvider {

    private static Gson GSON;

    private static Gson createJson(IgnoreSpec ignoreSpec) {
        GsonBuilder builder = createBuilder(ignoreSpec);
        return builder.create();
    }

    private static GsonBuilder createBuilder(IgnoreSpec ignoreSpec) {
        return new GsonBuilder()
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
                });
    }

    public static void setUp(Supplier<IgnoreSpec> ignoreSupplier) {
        GSON = createJson(ignoreSupplier.get());
    }

    public static Gson json() {
        if (GSON == null) {
            // Create with default configs
            GSON = createJson(null);
        }
        return GSON;
    }
}
