package com.mowbie.mowbie_backend.controller;

import com.mowbie.mowbie_backend.dto.ResponseDTO;
import com.mowbie.mowbie_backend.dto.UserDTO;
import com.mowbie.mowbie_backend.model.User;
import com.mowbie.mowbie_backend.repository.UserRepository;
import com.mowbie.mowbie_backend.security.Authorization;
import com.mowbie.mowbie_backend.security.Regex;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final String UPLOAD_DIR = "uploads/users/";
    private final Authorization auth = new Authorization();

    @GetMapping
    public ResponseEntity<?> getAllUsers(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        ResponseEntity<?> authResponse = auth.authorize(authHeader, "manager");
        if (authResponse != null) {
            return authResponse;
        }

        List<UserDTO> users = UserRepository.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.status(401)
                    .body(ResponseDTO.error(401, "USERS_NOT_FOUND", "Danh sách trống!"));
        }
        return ResponseEntity.ok(ResponseDTO.success("Danh sách người dùng", Map.of(
                 "users", users
        )));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long userId,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String newPassword,
            @RequestParam(required = false) MultipartFile avatar) {

        if (!(phoneNumber == null) && !Regex.isValidPhoneNumber(phoneNumber)){
            return ResponseEntity.status(400).body(ResponseDTO.error(400, "INVALID_PHONE","Số điện thoại không hợp lệ!"));
        }
        if (!(newPassword == null) && !Regex.isValidPassword(newPassword)){
            return ResponseEntity.status(400).body(ResponseDTO.error(400, "INVALID_PASSWORD","Mật khẩu quá yếu. Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường và số."));
        }
        String avatarPath = null;

        // Kiểm tra nếu có file avatar mới được tải lên
        if (avatar != null && !avatar.isEmpty()) {
            try {
                String fileName = avatar.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR, fileName);

                // Đảm bảo thư mục tồn tại
                Files.createDirectories(filePath.getParent());

                // Lưu file
                Files.copy(avatar.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Lưu đường dẫn tương đối vào database
                avatarPath = filePath.toString();
            } catch (IOException e) {
                return ResponseEntity.status(500).body(ResponseDTO.error(500, "UPLOAD_FAILED", "Không thể upload avatar!"));
            }
        }

        // Gọi repository để cập nhật thông tin user
        UserDTO user = UserRepository.updateProfile(userId, username, phoneNumber, newPassword, avatarPath);
        if (user == null) {
            return ResponseEntity.status(400).body(ResponseDTO.error(400, "FAILED_UPDATE", "Cập nhật không thành công!"));
        }

        return ResponseEntity.ok(ResponseDTO.success("Cập nhật thành công", Map.of("user", user)));
    }

    @PutMapping("/status")
    public ResponseEntity<?> updateUserStatus(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                              @RequestParam Long userId,
                                              @RequestParam Boolean status) {
        int result = UserRepository.updateUserStatusById(userId, status);
        if (result == 1) {
            return ResponseEntity.ok(ResponseDTO.success("Tài khoản đã được mở khóa thành công!", null));
        } else if (result == 0) {
            return ResponseEntity.ok(ResponseDTO.success("Đã khoá tài khoản thành công!", null));
        }
        return ResponseEntity.ok(ResponseDTO.error(400,"FAILED_UPDATE", "Cập nhật trạng thái tài khoản không thành công. Vui lòng thử lại!"));
    }
}

