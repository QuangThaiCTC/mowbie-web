package com.mowbie.mowbie_backend.controller;

import com.mowbie.mowbie_backend.dto.ResponseDTO;
import com.mowbie.mowbie_backend.model.ProductImage;
import com.mowbie.mowbie_backend.repository.ProductImageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

@RestController
@RequestMapping("/products/{productId}/image")
public class ProductImageController {
    private static final String UPLOAD_DIR = "uploads/products/";

    @PostMapping
    public ResponseEntity<?> addProductImage(@PathVariable Long productId, @RequestParam(required = false) MultipartFile productImage) {
        String productImagePath = null;

        // Kiểm tra nếu có file avatar mới được tải lên
        if (productImage != null && !productImage.isEmpty()) {
            try {
                String fileName = productImage.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR + productId + "/", fileName);

                // Đảm bảo thư mục tồn tại
                Files.createDirectories(filePath.getParent());

                // Lưu file
                Files.copy(productImage.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Lưu đường dẫn tương đối vào database
                productImagePath = filePath.toString();
                int result = ProductImageRepository.addProductImage(productId, productImagePath);
                if (result == 1) {
                    return ResponseEntity.ok(ResponseDTO.success("Thêm ảnh thành công!", Map.of("productImagePath", productImagePath)));
                }
            } catch (IOException e) {
                return ResponseEntity.status(500).body(ResponseDTO.error(500, "UPLOAD_FAILED", "Không thể upload!"));
            }
        }
        return ResponseEntity.status(500).body(ResponseDTO.error(500, "UPLOAD_FAILED", "Thêm hình ảnh không thành công!"));
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<?> deleteProductImage(@PathVariable Long productId, @PathVariable Long imageId) {
        int result = ProductImageRepository.deleteProductImage(imageId);
        if (result == 0) {
            return ResponseEntity.status(500).body(ResponseDTO.error(500, "DELETE_FAILED", "Xoá ảnh không thành công!"));
        }
        return ResponseEntity.ok(ResponseDTO.success("Xoá ảnh thành công!", null));
    }
}
