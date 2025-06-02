package com.anhpt.res_app.common.utils;

import com.github.slugify.Slugify;
import lombok.RequiredArgsConstructor;

import java.util.Map;

public final class CustomSlugify {
    private static final Slugify slugify = Slugify.builder().build();

    private CustomSlugify() {}

    public static String slugify(String input) {
        return slugify.slugify(removeVietnameseDiacritics(input));
    }

    private static String removeVietnameseDiacritics(String input) {
        if (input == null) return null;

        return input
            .replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a")
            .replaceAll("[èéẹẻẽêềếệểễ]", "e")
            .replaceAll("[ìíịỉĩ]", "i")
            .replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o")
            .replaceAll("[ùúụủũưừứựửữ]", "u")
            .replaceAll("[ỳýỵỷỹ]", "y")
            .replaceAll("đ", "d")
            .replaceAll("[ÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴ]", "A")
            .replaceAll("[ÈÉẸẺẼÊỀẾỆỂỄ]", "E")
            .replaceAll("[ÌÍỊỈĨ]", "I")
            .replaceAll("[ÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠ]", "O")
            .replaceAll("[ÙÚỤỦŨƯỪỨỰỬỮ]", "U")
            .replaceAll("[ỲÝỴỶỸ]", "Y")
            .replaceAll("Đ", "D");
    }
}
