package com.mowbie.mowbie_backend.controller;

import com.mowbie.mowbie_backend.dto.ProductDTO;
import com.mowbie.mowbie_backend.model.Product;
import com.mowbie.mowbie_backend.repository.ProductRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + File.separator + "uploads";

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        List<ProductDTO> products = ProductRepository.getAllProducts();
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "Không tìm thấy sản phẩm nào!"));
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable Long productId) {
        ProductDTO product = ProductRepository.getProductById(productId);
        if (product != null) {
            return ResponseEntity.ok(product);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Không tìm thấy sản phẩm này!"));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductDTO> products = ProductRepository.getProductsByCategory(categoryId);
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "Không tìm thấy sản phẩm nào trong danh mục này!"));
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/uploads/{filename}")
    public ResponseEntity<?> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(System.getProperty("user.dir"), "uploads", filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)  // Thay đổi nếu ảnh PNG
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestParam String productName,
                                        @RequestParam String description,
                                        @RequestParam BigDecimal price,
                                        @RequestParam Long categoryId,
                                        @RequestParam("file") MultipartFile file) {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        String fileName = file.getOriginalFilename();
        File destFile = new File(uploadDir, fileName);

        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Lỗi khi lưu file: " + e.getMessage()));
        }

        String filePath = "uploads/" + fileName;
        int result = ProductRepository.createProduct(productName, description, filePath, price, categoryId);

        if (result == 1) {
            return ResponseEntity.ok(Map.of("message", "Thêm sản phẩm thành công!"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Thêm sản phẩm không thành công!"));
    }
    @PutMapping("/update/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId,
                                           @RequestParam(required = false) String productName,
                                           @RequestParam(required = false) String description,
                                           @RequestParam(required = false) BigDecimal price,
                                           @RequestParam(required = false) Long categoryId,
                                           @RequestParam(value = "file", required = false) MultipartFile file) {
        // Kiểm tra sản phẩm có tồn tại không
        String oldImagePath = ProductRepository.getImagePathById(productId);
        if (oldImagePath == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Sản phẩm không tồn tại!"));
        }

        String newImagePath = oldImagePath;

        if (file != null && !file.isEmpty()) {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            String fileName = file.getOriginalFilename();
            File destFile = new File(uploadDir, fileName);

            try {
                // Xóa ảnh cũ nếu tồn tại
                File oldFile = new File(System.getProperty("user.dir") + File.separator + oldImagePath);
                if (oldFile.exists() && oldFile.isFile()) {
                    oldFile.delete();
                }

                // Lưu file mới
                file.transferTo(destFile);
                newImagePath = "uploads/" + fileName;
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Lỗi khi lưu file: " + e.getMessage()));
            }
        }

        int result = ProductRepository.updateProduct(productId, productName, description, newImagePath, price, categoryId);

        if (result == 1) {
            return ResponseEntity.ok(Map.of("message", "Cập nhật sản phẩm thành công!"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Cập nhật sản phẩm không thành công!"));
    }
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        String imagePath = ProductRepository.getImagePathById(productId);

        int result = ProductRepository.deleteProduct(productId);
        if (result == 1) {
            if (imagePath != null) {
                File imageFile = new File(System.getProperty("user.dir") + File.separator + imagePath);
                if (imageFile.exists() && imageFile.isFile()) {
                    imageFile.delete();  // XÓA FILE ẢNH KHI XÓA SẢN PHẨM
                }
            }
            return ResponseEntity.ok(Map.of("message", "Xóa sản phẩm thành công!"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Xóa sản phẩm không thành công!"));
    }
}


