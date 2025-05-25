package com.anhpt.res_app.common.model.enums.status;

import lombok.Getter;

@Getter
public enum CategoryStatus {
    ACTIVE("Hoạt động", "328E6E"),
    INACTIVE("Ngừng hoạt động", "CB0404");

    private final String label;
    private final String color;

    CategoryStatus(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public static CategoryStatus fromCode(String name) {
        for (CategoryStatus u : values()) {
            if (u.name().equalsIgnoreCase(name)) return u;
        }
        throw new IllegalArgumentException("Invalid CategoryStatus code: " + name);
    }
}
