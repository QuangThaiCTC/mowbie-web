package com.mowbie.mowbie_backend.controller;

import com.mowbie.mowbie_backend.dto.UserDTO;
import com.mowbie.mowbie_backend.model.User;
import com.mowbie.mowbie_backend.repository.UserRepository;
import com.mowbie.mowbie_backend.security.Regex;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<User> users = UserRepository.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "Không có người dùng nào!"));
        }

        List<UserDTO> userDTOs = users.stream()
                .map(user -> new UserDTO(user.getUserId(), user.getUsername(), user.getEmail(), user.getPhoneNumber(), user.getUserRole()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Long userId) {
        Optional<User> userOpt = UserRepository.getUserByIdOrEmail(userId, null);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            UserDTO userDTO = new UserDTO(user.getUserId(), user.getUsername(), user.getEmail(), user.getPhoneNumber(), user.getUserRole());
            return ResponseEntity.ok(userDTO);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Không tìm thấy người dùng này!"));
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestParam(required = false) String username, @RequestParam(required = false) String phoneNumber, @RequestParam(required = false) String password) {
        if (password != null && !password.isEmpty() && !Regex.isValidPassword(password)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Mật khẩu không hợp lệ!",
                    "hint", "Mật khẩu phải có chữ hoa, chữ thường, số và ít nhất 8 ký tự!"
            ));
        }

        int result = UserRepository.updateUser(userId, username, phoneNumber, password);
        if (result == 1) {
            return ResponseEntity.ok(Map.of("message", "Cập nhật người dùng thành công!"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Cập nhật người dùng không thành công!"));
        }
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        int result = UserRepository.deleteUser(userId);
        if (result == 1) {
            return ResponseEntity.ok(Map.of("message", "Xóa tài khoản thành công!"));
        } else if (result == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Người dùng không tồn tại!"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Xóa tài khoản không thành công!"));
    }
}

