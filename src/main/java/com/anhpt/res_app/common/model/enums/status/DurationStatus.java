package com.anhpt.res_app.common.model.enums.status;

import lombok.Getter;

@Getter
public enum DurationStatus {
    ACTIVE("Hoạt động", "328E6E"),
    INACTIVE("Ngừng hoạt động", "CB0404");

    private final String label;
    private final String color;

    DurationStatus(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public static DurationStatus fromCode(String name) {
        for (DurationStatus u : values()) {
            if (u.name().equalsIgnoreCase(name)) return u;
        }
        throw new IllegalArgumentException("Invalid DurationStatus code: " + name);
    }
}
