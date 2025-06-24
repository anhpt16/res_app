package com.anhpt.res_app.web.api;

import com.anhpt.res_app.common.dto.request.auth.LoginRequest;
import com.anhpt.res_app.common.dto.request.auth.RegisterRequest;
import com.anhpt.res_app.common.dto.response.ApiResponse;
import com.anhpt.res_app.common.dto.response.auth.LoginResponse;
import com.anhpt.res_app.web.service.WebAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class WebAuthApi {
    private final WebAuthService webAuthService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
        @RequestBody @Valid LoginRequest request
    ) {
        LoginResponse response = webAuthService.login(request);
        ApiResponse<LoginResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Đăng nhập thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(
        @RequestBody @Valid RegisterRequest request
    ) throws Exception {
        webAuthService.register(request);
        ApiResponse<Void> apiResponse = new ApiResponse<>(
            HttpStatus.CREATED.value(),
            true,
            "Đăng ký thành công",
            null
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }


}
