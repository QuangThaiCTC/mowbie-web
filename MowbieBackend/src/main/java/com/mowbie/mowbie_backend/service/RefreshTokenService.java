package com.mowbie.mowbie_backend.service;

import com.mowbie.mowbie_backend.repository.UserRepository;
import com.mowbie.mowbie_backend.security.JwtUtil;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RefreshTokenService {
    private final JwtUtil jwtUtil;

    public RefreshTokenService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // Tạo Refresh Token (JWT) có thời hạn 7 ngày
    public String createRefreshToken(String email) {
        return jwtUtil.generateToken(email, 7 * 24 * 60 * 60 * 1000); // 7 ngày
    }

    // Kiểm tra Refresh Token có hợp lệ không
    public boolean validateRefreshToken(String token) {
        return jwtUtil.isValidToken(token);
    }
}
