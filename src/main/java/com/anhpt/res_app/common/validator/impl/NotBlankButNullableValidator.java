package com.anhpt.res_app.common.validator.impl;

import com.anhpt.res_app.common.validator.NotBlankButNullable;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotBlankButNullableValidator implements ConstraintValidator<NotBlankButNullable, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || !value.trim().isEmpty();
    }
}
