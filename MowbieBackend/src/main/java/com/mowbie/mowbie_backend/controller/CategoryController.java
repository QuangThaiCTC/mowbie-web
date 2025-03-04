package com.mowbie.mowbie_backend.controller;

import com.mowbie.mowbie_backend.dto.CategoryDTO;
import com.mowbie.mowbie_backend.model.Category;
import com.mowbie.mowbie_backend.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        List<CategoryDTO> categories = CategoryRepository.getAllCategories();
        if (categories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "Không tìm thấy danh mục nào!"));
        }
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategory(@PathVariable Long categoryId) {
        CategoryDTO category = CategoryRepository.getCategoryById(categoryId);
        if (category != null) {
            return ResponseEntity.ok(category);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Không tìm thấy danh mục này!"));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestParam String categoryName, @RequestParam String description) {
        int result = CategoryRepository.createCategory(categoryName, description);
        if (result == 1) {
            return ResponseEntity.ok(Map.of("message", "Tạo danh mục thành công!"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Tạo danh mục không thành công!"));
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable Long categoryId, @RequestParam String categoryName, @RequestParam String description) {
        int result = CategoryRepository.updateCategory(categoryId, categoryName, description);
        if (result == 1) {
            return ResponseEntity.ok(Map.of("message", "Cập nhật danh mục thành công!"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Cập nhật danh mục không thành công!"));
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        int result = CategoryRepository.deleteCategory(categoryId);
        if (result == 1) {
            return ResponseEntity.ok(Map.of("message", "Xóa danh mục thành công!"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Xóa danh mục không thành công!"));
    }
}

