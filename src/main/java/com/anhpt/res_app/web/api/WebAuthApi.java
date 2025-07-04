package com.anhpt.res_app.web.api;

import com.anhpt.res_app.common.dto.request.auth.LoginRequest;
import com.anhpt.res_app.common.dto.request.auth.RegisterRequest;
import com.anhpt.res_app.common.dto.response.ApiResponse;
import com.anhpt.res_app.common.dto.response.auth.LoginResponse;
import com.anhpt.res_app.common.dto.response.auth.TokenExtractor;
import com.anhpt.res_app.common.utils.ApiCategory;
import com.anhpt.res_app.common.utils.ApiDescription;
import com.anhpt.res_app.web.service.WebAuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @Value("${jwt.access-token-validity}")
    private long accessTokenValidityInMs;

    @PostMapping("/login")
    @ApiDescription("Đăng nhập")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
        @RequestBody @Valid LoginRequest request,
        HttpServletResponse response
    ) {
        LoginResponse loginResponse = webAuthService.login(request);

        // Tạo HttpOnly Cookie
        Cookie accessTokenCookie = new Cookie("accessToken", loginResponse.getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(false);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge((int) (accessTokenValidityInMs / 1000));
//        if (!cookieDomain.equals("localhost")) accessTokenCookie.setDomain(cookieDomain);
        response.addCookie(accessTokenCookie);

        ApiResponse<LoginResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Đăng nhập thành công",
            loginResponse
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

    @GetMapping("/me")
    @ApiDescription("Lấy thông tin dựa trên Token")
    public ResponseEntity<ApiResponse<TokenExtractor>> extractToken(
        @CookieValue (name = "accessToken", required = false) String token
    ) {
        TokenExtractor tokenExtractor = webAuthService.extract(token);
        if (token == null) {
            throw new IllegalArgumentException("Token không hợp lệ");
        }
        ApiResponse<TokenExtractor> apiResponse = new ApiResponse<>(
            HttpStatus.CREATED.value(),
            true,
            "Lấy thông tin thành công",
            tokenExtractor
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("/logout")
    @ApiDescription("Đăng xuất")
    public ResponseEntity<ApiResponse<Void>> logout(
        @CookieValue (name = "accessToken", required = false) String token,
        HttpServletResponse response
    ) {
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(false);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0);
        response.addCookie(accessTokenCookie);
        ApiResponse<Void> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Đăng xuất thành công",
            null
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

}
