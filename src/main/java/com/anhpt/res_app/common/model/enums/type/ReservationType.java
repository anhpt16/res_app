package com.anhpt.res_app.common.model.enums.type;

import lombok.Getter;

@Getter
public enum ReservationType {
    WEBSITE("Website", "000"),
    CONTACT("Liên hệ", "000"),
    ONSITE("Trực tiếp", "000");

    private final String label;
    private final String color;

    ReservationType(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public static ReservationType fromCode(String name) {
        for (ReservationType u : values()) {
            if (u.name().equalsIgnoreCase(name)) return u;
        }
        throw new IllegalArgumentException("Invalid ReservationType code: " + name);
    }
}
