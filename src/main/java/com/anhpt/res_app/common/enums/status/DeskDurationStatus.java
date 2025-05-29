package com.anhpt.res_app.common.enums.status;

import lombok.Getter;

@Getter
public enum DeskDurationStatus {
    ACTIVE("Hoạt động", "328E6E"),
    INACTIVE("Ngừng hoạt động", "CB0404");

    private final String label;
    private final String color;

    DeskDurationStatus(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public static DeskDurationStatus fromCode(String name) {
        for (DeskDurationStatus u : values()) {
            if (u.name().equalsIgnoreCase(name)) return u;
        }
        throw new IllegalArgumentException("Invalid DeskDurationStatus code: " + name);
    }
}
