package com.anhpt.res_app.common.model.enums.status;

import lombok.Getter;

@Getter
public enum ReservationStatus {
    BOOKED("Đã đăt", "328E6E"),
    CANCELED("Đã hủy", "CB0404");

    private final String label;
    private final String color;

    ReservationStatus(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public static ReservationStatus fromCode(String name) {
        for (ReservationStatus u : values()) {
            if (u.name().equalsIgnoreCase(name)) return u;
        }
        throw new IllegalArgumentException("Invalid ReservationStatus code: " + name);
    }
}
