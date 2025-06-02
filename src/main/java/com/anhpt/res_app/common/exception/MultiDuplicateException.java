package com.anhpt.res_app.common.exception;

import java.util.Map;

public class MultiDuplicateException extends RuntimeException {
    private final Map<String, String> errors;

    public MultiDuplicateException(Map<String, String> errors) {
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
