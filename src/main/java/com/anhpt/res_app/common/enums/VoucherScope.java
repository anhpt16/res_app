package com.anhpt.res_app.common.enums;

import lombok.Getter;

@Getter
public enum VoucherScope {
    TABLE("Bàn", "000000"),
    PREORDER("Món ăn đặt trước", "000000"),
    ORDER("Món ăn đặt sau", "000000"),
    ALL("Toàn bộ hóa đơn", "000000");

    private final String label;
    private final String color;

    VoucherScope(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public static VoucherScope fromCode(String name) {
        for (VoucherScope u : values()) {
            if (u.name().equalsIgnoreCase(name)) return u;
        }
        throw new IllegalArgumentException("Invalid VoucherScope code: " + name);
    }
}
