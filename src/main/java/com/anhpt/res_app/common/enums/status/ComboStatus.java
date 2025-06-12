package com.anhpt.res_app.common.enums.status;

public enum ComboStatus {
    PUBLISHED("Đã phát hành", "000"),
    DRAFT("Nháp", "328E6E"),
    INACTIVE("Ngừng hoạt động", "CB0404");

    private final String label;
    private final String color;

    ComboStatus(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public static ComboStatus fromCode(String name) {
        for (ComboStatus u : values()) {
            if (u.name().equalsIgnoreCase(name)) return u;
        }
        throw new IllegalArgumentException("Invalid ComboStatus code: " + name);
    }
}
