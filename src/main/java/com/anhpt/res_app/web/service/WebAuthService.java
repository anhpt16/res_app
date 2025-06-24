package com.anhpt.res_app.web.service;

import com.anhpt.res_app.common.dto.request.auth.LoginRequest;
import com.anhpt.res_app.common.dto.request.auth.RegisterRequest;
import com.anhpt.res_app.common.dto.response.auth.LoginResponse;
import com.anhpt.res_app.common.dto.response.auth.RoleInfo;
import com.anhpt.res_app.common.dto.response.auth.UserInfo;
import com.anhpt.res_app.common.entity.Role;
import com.anhpt.res_app.common.entity.User;
import com.anhpt.res_app.common.entity.UserRole;
import com.anhpt.res_app.common.enums.status.UserStatus;
import com.anhpt.res_app.common.enums.type.UserType;
import com.anhpt.res_app.common.exception.BadCredentialsException;
import com.anhpt.res_app.common.repository.RoleRepository;
import com.anhpt.res_app.common.repository.UserRepository;
import com.anhpt.res_app.common.utils.Constants;
import com.anhpt.res_app.common.utils.JwtTokenProvider;
import com.anhpt.res_app.web.validation.WebAuthValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebAuthService {
    // Repository
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    // Validation
    private final WebAuthValidation webAuthValidation;
    // Other
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest request) {
        webAuthValidation.validateLogin(request);
        // Lấy ra tài khoản với tên đăng nhập
        Optional<User> user = userRepository.findByUsername(request.getUsername());
        if (user.isEmpty()) {
            throw new BadCredentialsException("Không tìm thấy User");
        }
        User currentUser = user.get();
        // Kiểm tra mật khẩu
        boolean isCorrectPassword = passwordEncoder.matches(request.getPassword(), currentUser.getPassword());
        if (!isCorrectPassword) {
            throw new BadCredentialsException("Không tìm thấy User");
        }
        // Lấy thông tin user
        UserInfo userInfo = UserInfo.builder()
            .id(currentUser.getId())
            .name(currentUser.getName())
            .build();
        // Lấy thông tin vai trò của user
        List<UserRole> userRoles = currentUser.getUserRoles();
        List<RoleInfo> roleInfos = userRoles.stream()
            .map(userRole -> {
                Integer roleId = userRole.getRole().getId();
                String roleName = userRole.getRole().getName();
                return RoleInfo.builder()
                    .id(roleId)
                    .name(roleName)
                    .build();
            })
            .collect(Collectors.toList());
        // Tạo Token
        String token = jwtTokenProvider.generateAccessToken(userInfo, roleInfos);
        return LoginResponse.builder().accessToken(token).build();
    }

    @Transactional
    public void register(RegisterRequest request) throws Exception {
        webAuthValidation.validateRegister(request);
        User user = new User();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setType(UserType.LOCAL);
        user.setStatus(UserStatus.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        // Tìm kiếm role: user
        Role role = roleRepository.findByName(Constants.USER_ROLE)
            .orElseThrow(() -> new Exception("Không tìm thấy Role mặc định"));
        // Gán role cho user
        UserRole userRole = new UserRole();
        userRole.setRole(role);
        userRole.setUser(user);
        userRole.setCreatedAt(LocalDateTime.now());
        user.getUserRoles().add(userRole);
        user = userRepository.save(user);
        if (user.getId() == null) {
            log.warn("Tạo tài khoản không thành công username: {}", user.getUsername());
            throw new Exception("Đã có lỗi xảy ra khi tạo tài khoản");
        }
    }
}
