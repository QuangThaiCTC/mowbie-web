package com.mowbie.mowbie_backend.controller;

import com.mowbie.mowbie_backend.dto.ResponseDTO;
import com.mowbie.mowbie_backend.dto.UserDTO;
import com.mowbie.mowbie_backend.repository.UserRepository;
import com.mowbie.mowbie_backend.security.JwtUtil;
import com.mowbie.mowbie_backend.security.Regex;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam("username") String username,
                                      @RequestParam("email") String email,
                                      @RequestParam("password") String password,
                                      @RequestParam("phone_number") String phoneNumber,
                                      @RequestParam("confirm_password") String confirmPassword) {

        if (!Regex.isValidEmail(email)) {
            return ResponseEntity.status(400).body(ResponseDTO.error(400, "INVALID_EMAIL","Email không hợp lệ. Vui lòng nhập email đúng định dạng như example@email.com"));
        }
        if (!Regex.isValidPassword(password)) {
            return ResponseEntity.status(400).body(ResponseDTO.error(400, "INVALID_PASSWORD","Mật khẩu quá yếu. Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường và số."));
        }
        if (!Regex.isValidPhoneNumber(phoneNumber)) {
            return ResponseEntity.status(400).body(ResponseDTO.error(400, "INVALID_PHONE","Số điện thoại không hợp lệ!"));
        }
        if (!password.equals(confirmPassword)) {
            return ResponseEntity.status(400).body(ResponseDTO.error(400, "ISN'T MATCH","Mật khẩu xác nhận không khớp!"));
        }

        int status = UserRepository.register(username, email, phoneNumber, password);
        return switch (status) {
            case 1 -> ResponseEntity.ok().body(ResponseDTO.success( "Đăng ký thành công! Bạn có thể đăng nhập ngay bây giờ.",null));
            case 0 -> ResponseEntity.status(400).body(ResponseDTO.error(400, "EMAIL_EXIST","Email đã được sử dụng. Vui lòng chọn email khác!"));
            default -> ResponseEntity.status(400).body(ResponseDTO.error(400, "SQL_EXCEPTION","Đã xảy ra lỗi, vui lòng thử lại!"));
        };
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestParam("email") String email,
                                    @RequestParam("password") String password,
                                    HttpServletResponse response) {
        if (!Regex.isValidEmail(email)) {
            return ResponseEntity.status(400).body(ResponseDTO.error(400, "INVALID_EMAIL","Email hoặc mật khẩu không đúng, vui lòng thử lại!"));
        }

        if (!Regex.isValidPassword(password)) {
            return ResponseEntity.status(400).body(ResponseDTO.error(400, "INVALID_PASSWORD","Email hoặc mật khẩu không đúng, vui lòng thử lại!"));
        }

        // Tìm user trong database
        UserDTO user = UserRepository.login(email, password);
        if (user == null) {
            return ResponseEntity.status(401)
                    .body(ResponseDTO.error(401, "USER_NOT_FOUND", "Email hoặc mật khẩu không đúng, vui lòng thử lại!"));
        }

        // Kiểm tra trạng thái tài khoản (nếu bị khóa thì không cho login)
        if (!user.getIsActive()) {
            return ResponseEntity.status(403)
                    .body(ResponseDTO.error(403, "ACCOUNT_LOCKED", "Tài khoản của bạn đã bị khóa!"));
        }
        JwtUtil jwtUtil = new JwtUtil();
        String accessToken = jwtUtil.generateToken(user.getEmail(), 15 * 60 * 1000); // 15 phút
        String refreshToken = jwtUtil.generateToken(user.getEmail(), 7 * 24 * 60 * 60 * 1000); // 7 ngày

        // Lưu refresh token vào HttpOnly Cookie
        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
        response.addCookie(cookie);

        return ResponseEntity.ok(ResponseDTO.success("Đăng nhập thành công", Map.of(
                "access_token", accessToken, "user", user
        )));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Xóa cookie refresh_token
        Cookie cookie = new Cookie("refresh_token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0); // Hết hạn ngay lập tức
        response.addCookie(cookie);

        return ResponseEntity.ok(ResponseDTO.success("Đăng xuất thành công", null));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@CookieValue("refresh_token") String refreshToken) {
        JwtUtil jwtUtil = new JwtUtil();
        if (!jwtUtil.isValidToken(refreshToken)) {
            return ResponseEntity.status(401).body(ResponseDTO.error(401, "INVALID_REFRESH_TOKEN", "Phiên đăng nhập hết hạn, vui lòng đăng nhập lại."));
        }

        String email = jwtUtil.extractEmail(refreshToken);
        String newAccessToken = jwtUtil.generateToken(email, 15 * 60 * 1000); // 15 phút
        UserDTO user = UserRepository.getUserByEmailOrId(email, null);

        if (user == null) {
            return ResponseEntity.status(401).body(ResponseDTO.error(401, "INVALID_REFRESH_TOKEN", "Phiên đăng nhập hết hạn, vui lòng đăng nhập lại."));
        }
        if (!user.getIsActive()) {
            return ResponseEntity.status(403)
                    .body(ResponseDTO.error(403, "ACCOUNT_LOCKED", "Tài khoản của bạn đã bị khóa!"));
        }

        return ResponseEntity.ok(ResponseDTO.success("Làm mới token thành công", Map.of(
                "access_token", newAccessToken, "user", user
        )));
    }
}

