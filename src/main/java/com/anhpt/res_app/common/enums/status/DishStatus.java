package com.anhpt.res_app.common.enums.status;

import lombok.Getter;

@Getter
public enum DishStatus {
    ACTIVE("Hoạt động", "328E6E"),
    INACTIVE("Ngừng hoạt động", "CB0404");

    private final String label;
    private final String color;

    DishStatus(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public static DishStatus fromCode(String name) {
        for (DishStatus u : values()) {
            if (u.name().equalsIgnoreCase(name)) return u;
        }
        throw new IllegalArgumentException("Invalid DishStatus code: " + name);
    }
}
