package com.anhpt.res_app.common.model.enums.status;

import lombok.Getter;

@Getter
public enum NewStatus {
    PUBLISHED("Phát hành", "328E6E"),
    DRAFT("Bản nháp", "CB0404"),
    INACTIVE("Ngừng hoạt động", "CB0404");

    private final String label;
    private final String color;

    NewStatus(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public static NewStatus fromCode(String name) {
        for (NewStatus u : values()) {
            if (u.name().equalsIgnoreCase(name)) return u;
        }
        throw new IllegalArgumentException("Invalid NewStatus code: " + name);
    }
}
