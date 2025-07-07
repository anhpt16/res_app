package com.anhpt.res_app.common.enums;

import lombok.Getter;

@Getter
public enum DeskType {
    D_RECT("Bàn chữ nhật", "000"),
    D_ROUND("Bàn tròn", "000"),
    D_SQUARE("Bàn vuông", "000"),
    D_T("Bàn chữ T", "000"),
    D_U("Bàn chữ U", "000"),
    D_OVAL("Bàn bầu dục", "000"),
    D_REACT_LONG("Bàn chữ nhật dài", "000"),
    D_BAR("Bàn quầy bar", "000"),
    D_BUFFET("Bàn tiệc Buffet", "000");

    private final String label;
    private final String color;

    DeskType(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public static DeskType fromCode(String name) {
        for (DeskType u : values()) {
            if (u.name().equalsIgnoreCase(name)) return u;
        }
        throw new IllegalArgumentException("Invalid DeskType code: " + name);
    }
}
