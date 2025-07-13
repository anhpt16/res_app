package com.anhpt.res_app.common.enums.status;

import lombok.Getter;

@Getter
public enum DeskMessageStatus {
    EMPTY("Bàn trống", "328E6E"),
    VIEW("Bàn đang xem", "CB0404"),
    BOOKED("Bàn đã đặt", "000");

    private final String label;
    private final String color;

    DeskMessageStatus(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public static DeskMessageStatus fromCode(String name) {
        for (DeskMessageStatus u : values()) {
            if (u.name().equalsIgnoreCase(name)) return u;
        }
        throw new IllegalArgumentException("Invalid DeskMessageStatus code: " + name);
    }
}
