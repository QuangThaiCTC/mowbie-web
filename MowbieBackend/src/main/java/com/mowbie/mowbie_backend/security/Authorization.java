package com.mowbie.mowbie_backend.security;

import com.mowbie.mowbie_backend.dto.ResponseDTO;
import com.mowbie.mowbie_backend.dto.UserDTO;
import com.mowbie.mowbie_backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;

public class Authorization {
    private final JwtUtil jwtUtil;

    public Authorization() {
        this.jwtUtil = new JwtUtil();
    }

    public ResponseEntity<?> authorize(String authHeader, String requiredRole) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(ResponseDTO.error(401, "MISSING_TOKEN", "Vui lòng đăng nhập để tiếp tục!"));
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body(ResponseDTO.error(401, "INVALID_TOKEN", "Phiên đăng nhập hết hạn hoặc token không hợp lệ!"));
        }

        String email = jwtUtil.extractEmail(token);
        UserDTO user = UserRepository.getUserByEmailOrId(email, null);

        if (user == null) {
            return ResponseEntity.status(401).body(ResponseDTO.error(401, "USER_NOT_FOUND", "Người dùng không tồn tại!"));
        }

        if (!user.getIsActive()) {
            return ResponseEntity.status(403).body(ResponseDTO.error(403, "ACCOUNT_LOCKED", "Tài khoản đã bị khóa!"));
        }

        if (requiredRole != null && !user.getUserRole().equalsIgnoreCase(requiredRole)) {
            return ResponseEntity.status(403).body(ResponseDTO.error(403, "FORBIDDEN", "Bạn không có quyền thực hiện thao tác này!"));
        }

        return null;
    }
}
