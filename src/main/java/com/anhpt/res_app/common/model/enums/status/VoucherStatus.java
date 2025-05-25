package com.anhpt.res_app.common.model.enums.status;

import lombok.Getter;

@Getter
public enum VoucherStatus {
    ACTIVE("Hoạt động", "328E6E"),
    INACTIVE("Ngừng hoạt động", "CB0404");

    private final String label;
    private final String color;

    VoucherStatus(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public static VoucherStatus fromCode(String name) {
        for (VoucherStatus u : values()) {
            if (u.name().equalsIgnoreCase(name)) return u;
        }
        throw new IllegalArgumentException("Invalid VoucherStatus code: " + name);
    }
}
