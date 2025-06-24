package com.anhpt.res_app.web.validation;

import com.anhpt.res_app.common.dto.request.auth.LoginRequest;
import com.anhpt.res_app.common.dto.request.auth.RegisterRequest;
import com.anhpt.res_app.common.entity.User;
import com.anhpt.res_app.common.exception.BadCredentialsException;
import com.anhpt.res_app.common.exception.MultiDuplicateException;
import com.anhpt.res_app.common.repository.UserRepository;
import com.anhpt.res_app.common.utils.function.FieldNameUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebAuthValidation {
    // Repository
    private final UserRepository userRepository;
    // Other
    private final PasswordEncoder passwordEncoder;

    public void validateLogin(LoginRequest request) {
    }

    public void validateRegister(RegisterRequest request) {
        Map<String, String> errors = new HashMap<>();
        // Kiểm tra username đã tồn tại
        boolean isUsernameExist = userRepository.existsByUsername(request.getUsername());
        if (isUsernameExist) {
            String field = FieldNameUtil.getFieldName(RegisterRequest::getUsername);
            errors.put(field, "Đã tồn tại");
        }
        if (errors.size() > 0) {
            throw new MultiDuplicateException(errors);
        }
    }
}
