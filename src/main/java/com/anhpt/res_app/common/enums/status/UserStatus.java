package com.anhpt.res_app.common.enums.status;

import lombok.Getter;

@Getter
public enum UserStatus {
    ACTIVE("Hoạt động", "328E6E"),
    BANNED("Bị cấm", "000"),
    INACTIVE("Ngừng hoạt động", "CB0404");

    private final String label;
    private final String color;

    UserStatus(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public static UserStatus fromCode(String name) {
        for (UserStatus u : values()) {
            if (u.name().equalsIgnoreCase(name)) return u;
        }
        throw new IllegalArgumentException("Invalid UserStatus code: " + name);
    }
}
