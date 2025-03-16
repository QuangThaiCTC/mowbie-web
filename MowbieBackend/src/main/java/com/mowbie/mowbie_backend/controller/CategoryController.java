package com.mowbie.mowbie_backend.controller;

import com.mowbie.mowbie_backend.dto.ResponseDTO;
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
        List<Category> categories = CategoryRepository.getAllCategory();
        if (categories == null) {
            return ResponseEntity.status(401)
                    .body(ResponseDTO.error(401, "CATEGORIES_NOT_FOUND", "Không tìm thấy danh mục nào!"));
        }
        return ResponseEntity.ok(ResponseDTO.success("Lấy danh mục thành công", Map.of(
                 "categories", categories
        )));
    }
}

