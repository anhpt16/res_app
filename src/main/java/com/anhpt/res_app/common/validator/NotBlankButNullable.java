package com.anhpt.res_app.common.validator;

import com.anhpt.res_app.common.validator.impl.NotBlankButNullableValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotBlankButNullableValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlankButNullable {
    String message() default "Không được rỗng nếu đã nhập";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
