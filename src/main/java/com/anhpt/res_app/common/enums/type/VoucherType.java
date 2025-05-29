package com.anhpt.res_app.common.enums.type;

import lombok.Getter;

@Getter
public enum VoucherType {
    PERCENT("Phần trăm", "000000"),
    FIXED("Cố định", "000000");

    private final String label;
    private final String color;

    VoucherType(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public static VoucherType fromCode(String name) {
        for (VoucherType u : values()) {
            if (u.name().equalsIgnoreCase(name)) return u;
        }
        throw new IllegalArgumentException("Invalid VoucherType code: " + name);
    }
}
