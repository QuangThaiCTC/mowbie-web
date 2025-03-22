package com.mowbie.mowbie_backend.controller;

import com.mowbie.mowbie_backend.dto.ResponseDTO;
import com.mowbie.mowbie_backend.model.Category;
import com.mowbie.mowbie_backend.repository.CategoryRepository;
import com.mowbie.mowbie_backend.security.Authorization;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final Authorization auth = new Authorization();

    // Lấy tất cả danh mục
    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        List<Category> categories = CategoryRepository.getAllCategory();
        if (categories == null) {
            return ResponseEntity.status(404)
                    .body(ResponseDTO.error(404, "CATEGORIES_NOT_FOUND", "Không tìm thấy danh mục nào!"));
        }
        return ResponseEntity.ok(ResponseDTO.success("Lấy danh mục thành công", Map.of("categories", categories)));
    }

    @PostMapping
    public ResponseEntity<?> addCategory(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                         @RequestParam String categoryName) {
        ResponseEntity<?> authResponse = auth.authorize(authHeader, "manager");
        if (authResponse != null) {
            return authResponse;
        }

        if (categoryName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(ResponseDTO.error(400, "INVALID_NAME", "Tên danh mục không hợp lệ!"));
        }

        int result = CategoryRepository.addCategory(null, categoryName);
        if (result == 1) {
            return ResponseEntity.ok(ResponseDTO.success("Thêm danh mục thành công!", null));
        }
        return ResponseEntity.status(400).body(ResponseDTO.error(400, "ADD_FAILED", "Thêm danh mục không thành công!"));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                            @PathVariable Long categoryId,
                                            @RequestParam String categoryName) {
        ResponseEntity<?> authResponse = auth.authorize(authHeader, "manager");
        if (authResponse != null) {
            return authResponse;
        }
        int result = CategoryRepository.updateCategory(categoryId, categoryName);
        if (result == 1) {
            return ResponseEntity.ok(ResponseDTO.success("Cập nhật danh mục thành công!", null));
        }
        return ResponseEntity.status(400).body(ResponseDTO.error(400, "UPDATE_FAILED", "Cập nhật danh mục không thành công!"));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                            @PathVariable Long categoryId) {
        ResponseEntity<?> authResponse = auth.authorize(authHeader, "manager");
        if (authResponse != null) {
            return authResponse;
        }

        int result = CategoryRepository.deleteCategory(categoryId, null);
        if (result == 1) {
            return ResponseEntity.ok(ResponseDTO.success("Xóa danh mục thành công!", null));
        }
        return ResponseEntity.status(400).body(ResponseDTO.error(400, "DELETE_FAILED", "Xóa danh mục không thành công!"));
    }
}
