package com.mowbie.mowbie_backend.controller;

import com.mowbie.mowbie_backend.model.User;
import com.mowbie.mowbie_backend.repository.UserRepository;
import com.mowbie.mowbie_backend.security.JwtUtil;
import com.mowbie.mowbie_backend.security.Regex;
import com.mowbie.mowbie_backend.security.SecurityInfo;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam("email") String email,
                                   @RequestParam("password") String password,
                                   HttpServletResponse response,
                                   HttpServletRequest request) {

        // Kiểm tra token trong cookie
        Optional<String> existingToken = getTokenFromRequest(request);
        if (existingToken.isPresent() && JwtUtil.validateToken(existingToken.get())) {
            String emailFromToken = JwtUtil.getEmailFromJWT(existingToken.get());
            if (emailFromToken.equals(email)) {
                Optional<User> user = UserRepository.getUserByIdOrEmail(null, emailFromToken);
                if (user.isPresent()) {
                    return ResponseEntity.ok(user.get());
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Email hoặc mật khẩu không chính xác!"));
            }
        }

        // Kiểm tra email & password hợp lệ
        if (!Regex.isValidEmail(email) || !Regex.isValidPassword(password)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email hoặc mật khẩu không chính xác!"));
        }

        // Kiểm tra user trong DB
        Optional<User> userOptional = UserRepository.getUserByIdOrEmail(null, email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (SecurityInfo.verifyPassword(password, user.getPassword())) {
                String newToken = JwtUtil.generateToken(email);
                setCookie(response, "JWT", newToken, 3600);
                return ResponseEntity.ok(user);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Email hoặc mật khẩu không chính xác!"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam("username") String username,
                                      @RequestParam("email") String email,
                                      @RequestParam("password") String password,
                                      @RequestParam("phone_number") String phoneNumber,
                                      @RequestParam("confirm_password") String confirmPassword) {

        if (!Regex.isValidEmail(email)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email không hợp lệ!"));
        }
        if (!Regex.isValidPassword(password)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Mật khẩu không hợp lệ!"));
        }
        if (!Regex.isValidPhoneNumber(phoneNumber)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Số điện thoại không hợp lệ!"));
        }
        if (!password.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Mật khẩu không trùng khớp!"));
        }

        int status = UserRepository.createUser(username, email, phoneNumber, password);
        return switch (status) {
            case 1 -> ResponseEntity.ok(Map.of("message", "Tạo tài khoản thành công!"));
            case 0 -> ResponseEntity.badRequest().body(Map.of("message", "Email này đã tồn tại!"));
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Có lỗi xảy ra, vui lòng thử lại!"));
        };
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        setCookie(response, "JWT", "", 0);
        return ResponseEntity.ok(Map.of("message", "Đã đăng xuất!"));
    }

    private Optional<String> getTokenFromRequest(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("JWT".equals(cookie.getName())) {
                    return Optional.of(cookie.getValue());
                }
            }
        }
        return Optional.empty();
    }

    private void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Bật true nếu dùng HTTPS
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
}

