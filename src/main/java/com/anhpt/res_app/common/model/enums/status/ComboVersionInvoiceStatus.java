package com.anhpt.res_app.common.model.enums.status;

import lombok.Getter;

@Getter
public enum ComboVersionInvoiceStatus {
    ACTIVE("Hoạt động", "000"),
    DELETED("Đã xóa", "000"),
    CANCELED("Đã hủy", "000");

    private final String label;
    private final String color;

    ComboVersionInvoiceStatus(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public static ComboVersionInvoiceStatus fromCode(String name) {
        for (ComboVersionInvoiceStatus u : values()) {
            if (u.name().equalsIgnoreCase(name)) return u;
        }
        throw new IllegalArgumentException("Invalid ComboVersionInvoiceStatus code: " + name);
    }
}
