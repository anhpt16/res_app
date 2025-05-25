package com.anhpt.res_app.common.model.enums.type;

import lombok.Getter;

@Getter
public enum DishInvoiceType {
    PREORDER("Đặt trước", "000"),
    ORDER("Đặt sau", "000");

    private final String label;
    private final String color;

    DishInvoiceType(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public static DishInvoiceType fromCode(String name) {
        for (DishInvoiceType u : values()) {
            if (u.name().equalsIgnoreCase(name)) return u;
        }
        throw new IllegalArgumentException("Invalid DishInvoiceType code: " + name);
    }
}
