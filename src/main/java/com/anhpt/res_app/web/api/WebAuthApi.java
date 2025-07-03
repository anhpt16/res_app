package com.anhpt.res_app.web.api;

import com.anhpt.res_app.common.dto.request.auth.LoginRequest;
import com.anhpt.res_app.common.dto.request.auth.RegisterRequest;
import com.anhpt.res_app.common.dto.response.ApiResponse;
import com.anhpt.res_app.common.dto.response.auth.LoginResponse;
import com.anhpt.res_app.common.utils.ApiCategory;
import com.anhpt.res_app.common.utils.ApiDescription;
import com.anhpt.res_app.web.service.WebAuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
@ApiCategory(ApiCategory.CategoryType.PUBLIC)
public class WebAuthApi {
    private final WebAuthService webAuthService;

    @Value("${jwt.cookie.domain:localhost}")
    private String cookieDomain;
    @Value("${jwt.cookie.secure:false}")
    private boolean cookieSecure;


    @PostMapping("/login")
    @ApiDescription("Đăng nhập")
    public ResponseEntity<ApiResponse<Void>> login(
        @RequestBody @Valid LoginRequest request,
        HttpServletResponse response
    ) {
        LoginResponse loginResponse = webAuthService.login(request);

        // Tạo HttpOnly Cookie
        Cookie accessTokenCookie = new Cookie("accessToken", loginResponse.getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(false);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
//        if (!cookieDomain.equals("localhost")) accessTokenCookie.setDomain(cookieDomain);
        response.addCookie(accessTokenCookie);

        ApiResponse<Void> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Đăng nhập thành công",
            null
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/register")
    @ApiDescription("Đăng ký tài khoản")
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
