package com.anhpt.res_app.common.enums;

import com.anhpt.res_app.common.enums.status.CollectionStatus;

public enum FeatureMethod {
    GET,POST,PUT,DELETE,PATCH;

    public static FeatureMethod fromCode(String name) {
        for (FeatureMethod u : values()) {
            if (u.name().equalsIgnoreCase(name)) return u;
        }
        throw new IllegalArgumentException("Invalid FeatureMethod name: " + name);
    }
}
