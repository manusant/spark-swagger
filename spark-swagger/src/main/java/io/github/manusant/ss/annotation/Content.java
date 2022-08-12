package io.github.manusant.ss.annotation;

import io.github.manusant.ss.model.ContentType;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.TYPE_PARAMETER, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Content {
    ContentType value() default ContentType.APPLICATION_JSON;
}
