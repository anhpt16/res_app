package com.anhpt.res_app.common.model.enums.type;

import lombok.Getter;

@Getter
public enum UserType {
    LOCAL("Hệ thống", "D5451B"),
    GOOGLE("Google", "FF9F00"),
    FACEBOOK("Facebook", "5409DA");

    private final String label;
    private final String color;

    UserType(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public static UserType fromCode(String name) {
        for (UserType u : values()) {
            if (u.name().equalsIgnoreCase(name)) return u;
        }
        throw new IllegalArgumentException("Invalid UserType code: " + name);
    }

}
