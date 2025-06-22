package com.anhpt.res_app.common.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiCategory {
    CategoryType value();

    enum CategoryType {
        PUBLIC, USER, ADMIN
    }
}
