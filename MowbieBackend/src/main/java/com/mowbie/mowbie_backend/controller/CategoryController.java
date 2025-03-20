package com.mowbie.mowbie_backend.controller;

import com.mowbie.mowbie_backend.dto.ResponseDTO;
import com.mowbie.mowbie_backend.repository.CategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    // Lấy tất cả danh mục
    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        List<com.mowbie.mowbie_backend.model.Category> categories = CategoryRepository.getAllCategory();
        if (categories == null) {
            return ResponseEntity.status(404)
                    .body(ResponseDTO.error(404, "CATEGORIES_NOT_FOUND", "Không tìm thấy danh mục nào!"));
        }
        return ResponseEntity.ok(ResponseDTO.success("Lấy danh mục thành công", Map.of("categories", categories)));
    }

    // Thêm danh mục mới (Dùng RequestParam)
    @PostMapping
    public ResponseEntity<?> addCategory(@RequestParam String categoryName) {
        if (categoryName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(ResponseDTO.error(400, "INVALID_NAME", "Tên danh mục không hợp lệ!"));
        }

        int result = CategoryRepository.addCategory(null, categoryName);
        if (result == 1) {
            return ResponseEntity.ok(ResponseDTO.success("Thêm danh mục thành công!", null));
        }
        return ResponseEntity.status(400).body(ResponseDTO.error(400, "ADD_FAILED", "Thêm danh mục không thành công!"));
    }

    // Cập nhật danh mục (Dùng RequestParam)
    @PutMapping
    public ResponseEntity<?> updateCategory(@RequestParam Long categoryId, @RequestParam String categoryName) {
        int result = CategoryRepository.updateCategory(categoryId, categoryName);
        if (result == 1) {
            return ResponseEntity.ok(ResponseDTO.success("Cập nhật danh mục thành công!", null));
        }
        return ResponseEntity.status(400).body(ResponseDTO.error(400, "UPDATE_FAILED", "Cập nhật danh mục không thành công!"));
    }

    // Xóa danh mục (Dùng RequestParam)
    @DeleteMapping
    public ResponseEntity<?> deleteCategory(@RequestParam Long categoryId) {
        int result = CategoryRepository.deleteCategory(categoryId, null);
        if (result == 1) {
            return ResponseEntity.ok(ResponseDTO.success("Xóa danh mục thành công!", null));
        }
        return ResponseEntity.status(400).body(ResponseDTO.error(400, "DELETE_FAILED", "Xóa danh mục không thành công!"));
    }
}
