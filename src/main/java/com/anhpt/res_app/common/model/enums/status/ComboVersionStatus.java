package com.anhpt.res_app.common.model.enums.status;

import lombok.Getter;

@Getter
public enum ComboVersionStatus {
    ACTIVE("Hoạt động", "328E6E"),
    INACTIVE("Ngừng hoạt động", "CB0404");

    private final String label;
    private final String color;

    ComboVersionStatus(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public static ComboVersionStatus fromCode(String name) {
        for (ComboVersionStatus u : values()) {
            if (u.name().equalsIgnoreCase(name)) return u;
        }
        throw new IllegalArgumentException("Invalid ComboVersionStatus code: " + name);
    }
}
