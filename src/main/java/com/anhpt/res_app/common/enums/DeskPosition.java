package com.anhpt.res_app.common.enums;

import lombok.Getter;

@Getter
public enum DeskPosition {
    LEVEL_1("Tầng 1", "000"),
    LEVEL_2("Tầng 2", "000"),
    LEVEL_3("Tầng 3", "000");

    private final String label;
    private final String color;

    DeskPosition(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public static DeskPosition fromCode(String name) {
        for (DeskPosition u : values()) {
            if (u.name().equalsIgnoreCase(name)) return u;
        }
        throw new IllegalArgumentException("Invalid DeskPosition code: " + name);
    }
}
