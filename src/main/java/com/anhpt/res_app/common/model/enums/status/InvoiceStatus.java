package com.anhpt.res_app.common.model.enums.status;

import lombok.Getter;

@Getter
public enum InvoiceStatus {
    DRAFT("Nháp", "000"),
    DEPOSITED("TT. một phần", "000"),
    COMPLETED("Hoàn thành", "000"),
    REFUNDED("Hoàn tiền", "000"),
    CANCELED("Đã hủy", "000");

    private final String label;
    private final String color;

    InvoiceStatus(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public static InvoiceStatus fromCode(String name) {
        for (InvoiceStatus u : values()) {
            if (u.name().equalsIgnoreCase(name)) return u;
        }
        throw new IllegalArgumentException("Invalid InvoiceStatus code: " + name);
    }
}
