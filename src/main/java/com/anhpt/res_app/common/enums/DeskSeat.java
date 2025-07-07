package com.anhpt.res_app.common.enums;

import com.anhpt.res_app.common.enums.status.ComboVersionInvoiceStatus;
import lombok.Getter;

@Getter
public enum DeskSeat {
    SEAT_2("2 người", "000"),
    SEAT_2_4("2 - 4 người", "000"),
    SEAT_4_6("4 - 6 người", "000"),
    SEAT_4_8("4 - 8 người", "000"),
    SEAT_6_10("6 - 10 người", "000");


    private final String label;
    private final String color;

    DeskSeat(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public static DeskSeat fromCode(String name) {
        for (DeskSeat u : values()) {
            if (u.name().equalsIgnoreCase(name)) return u;
        }
        throw new IllegalArgumentException("Invalid DeskSeat code: " + name);
    }
}
