package com.anhpt.res_app.common.enums.status;


import lombok.Getter;

@Getter
public enum RoleStatus {
    ACTIVE("Hoạt động", "328E6E"),
    INACTIVE("Ngừng hoạt động", "CB0404");

    private final String label;
    private final String color;

    RoleStatus(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public static RoleStatus fromCode(String name) {
        for (RoleStatus u : values()) {
            if (u.name().equalsIgnoreCase(name)) return u;
        }
        throw new IllegalArgumentException("Invalid RoleStatus code: " + name);
    }
}
