package com.anhpt.res_app.common.utils;


import java.time.LocalDate;
import java.time.LocalDateTime;

public class RedisCastUtils {
    @SuppressWarnings("unchecked")
    public static <T> T castSafe(Object raw, Class<T> clazz) {
        if (raw == null) return null;

        // Long
        if (clazz == Long.class) {
            if (raw instanceof Long) return (T) raw;
            if (raw instanceof Integer) return (T) Long.valueOf((Integer) raw);
            if (raw instanceof String) return (T) Long.valueOf((String) raw);
        }

        // Integer
        if (clazz == Integer.class) {
            if (raw instanceof Integer) return (T) raw;
            if (raw instanceof Long) {
                Long val = (Long) raw;
                if (val <= Integer.MAX_VALUE && val >= Integer.MIN_VALUE)
                    return (T) Integer.valueOf(val.intValue());
                throw new IllegalArgumentException("Value out of Integer range");
            }
            if (raw instanceof String) return (T) Integer.valueOf((String) raw);
        }

        // Double
        if (clazz == Double.class) {
            if (raw instanceof Double) return (T) raw;
            if (raw instanceof Integer) return (T) Double.valueOf((Integer) raw);
            if (raw instanceof Long) return (T) Double.valueOf((Long) raw);
            if (raw instanceof String) return (T) Double.valueOf((String) raw);
        }

        // Float
        if (clazz == Float.class) {
            if (raw instanceof Float) return (T) raw;
            if (raw instanceof Integer) return (T) Float.valueOf((Integer) raw);
            if (raw instanceof Long) return (T) Float.valueOf((Long) raw);
            if (raw instanceof Double) return (T) Float.valueOf(((Double) raw).floatValue());
            if (raw instanceof String) return (T) Float.valueOf((String) raw);
        }

        // Boolean
        if (clazz == Boolean.class) {
            if (raw instanceof Boolean) return (T) raw;
            if (raw instanceof String) return (T) Boolean.valueOf((String) raw);
        }

        // String
        if (clazz == String.class) {
            return (T) raw.toString();
        }

        // LocalDateTime
        if (clazz == LocalDateTime.class && raw instanceof String) {
            return (T) LocalDateTime.parse((String) raw);
        }

        // LocalDate
        if (clazz == LocalDate.class && raw instanceof String) {
            return (T) LocalDate.parse((String) raw);
        }

        // Mặc định: cast trực tiếp
        return clazz.cast(raw);
    }
}
