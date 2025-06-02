package com.anhpt.res_app.common.enums.status;

import lombok.Getter;

@Getter
public enum PostStatus {
    PUBLISHED("Phát hành", "328E6E"),
    DRAFT("Bản nháp", "CB0404"),
    INACTIVE("Ngừng hoạt động", "CB0404");

    private final String label;
    private final String color;

    PostStatus(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public static PostStatus fromCode(String name) {
        for (PostStatus u : values()) {
            if (u.name().equalsIgnoreCase(name)) return u;
        }
        throw new IllegalArgumentException("Invalid PostStatus code: " + name);
    }
}
