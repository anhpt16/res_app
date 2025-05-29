package com.anhpt.res_app.common.enums.type;

import lombok.Getter;

@Getter
public enum ComboVersionInvoiceType {
    PREORDER("Đặt trước", "000"),
    ORDER("Đặt sau", "000");

    private final String label;
    private final String color;

    ComboVersionInvoiceType(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public static ComboVersionInvoiceType fromCode(String name) {
        for (ComboVersionInvoiceType u : values()) {
            if (u.name().equalsIgnoreCase(name)) return u;
        }
        throw new IllegalArgumentException("Invalid ComboVersionInvoiceType code: " + name);
    }
}
