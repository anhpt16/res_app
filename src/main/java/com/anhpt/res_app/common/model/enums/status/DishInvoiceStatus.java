package com.anhpt.res_app.common.model.enums.status;

import lombok.Getter;

@Getter
public enum DishInvoiceStatus {
    ACTIVE("Hoạt động", "000"),
    DELETED("Đã xóa", "000"),
    CANCELED("Đã hủy", "000");

    private final String label;
    private final String color;

    DishInvoiceStatus(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public static DishInvoiceStatus fromCode(String name) {
        for (DishInvoiceStatus u : values()) {
            if (u.name().equalsIgnoreCase(name)) return u;
        }
        throw new IllegalArgumentException("Invalid DishInvoiceStatus code: " + name);
    }
}
