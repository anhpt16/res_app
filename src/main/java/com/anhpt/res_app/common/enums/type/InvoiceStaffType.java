package com.anhpt.res_app.common.enums.type;

import lombok.Getter;

@Getter
public enum InvoiceStaffType {
    CREATED("Tạo mới", "000"),
    UPDATED("Cập nhật", "000");

    private final String label;
    private final String color;

    InvoiceStaffType(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public static InvoiceStaffType fromCode(String name) {
        for (InvoiceStaffType u : values()) {
            if (u.name().equalsIgnoreCase(name)) return u;
        }
        throw new IllegalArgumentException("Invalid InvoiceStaffType code: " + name);
    }
}
