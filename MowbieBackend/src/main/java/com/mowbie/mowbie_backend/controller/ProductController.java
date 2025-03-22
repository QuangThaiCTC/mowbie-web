package com.mowbie.mowbie_backend.controller;

import com.mowbie.mowbie_backend.dto.ProductDTO;
import com.mowbie.mowbie_backend.dto.ProductDetail;
import com.mowbie.mowbie_backend.dto.ResponseDTO;
import com.mowbie.mowbie_backend.model.Product;
import com.mowbie.mowbie_backend.repository.ProductRepository;
import com.mowbie.mowbie_backend.security.Authorization;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final Authorization auth = new Authorization();

    @GetMapping
    public static ResponseEntity<?> getAllProducts() {
        List<ProductDTO> products = ProductRepository.getAllProducts();
        if (products == null) {
            return ResponseEntity.status(404)
                    .body(ResponseDTO.error(404, "PRODUCTS_NOT_FOUND", "Không tìm thấy sản phẩm nào!"));
        }
        return ResponseEntity.ok(ResponseDTO.success("Lấy danh mục thành công", Map.of("products", products)));
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                           @RequestParam String productName,
                                           @RequestParam String productDescription,
                                           @RequestParam BigDecimal productPrice,
                                           @RequestParam Long categoryId) {
        ResponseEntity<?> authResponse = auth.authorize(authHeader, "manager");
        if (authResponse != null) {
            return authResponse;
        }

        int result = ProductRepository.createProduct(productName, productDescription, productPrice, categoryId);
        if (result == 1) {
            return ResponseEntity.ok(ResponseDTO.success("Thêm sản phẩm thành công!", null));
        }
        return ResponseEntity.status(400).body(ResponseDTO.error(400, "ADD_FAILED", "Thêm sản phẩm không thành công!"));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                           @PathVariable Long productId,
                                           @RequestParam(required = false) String productName,
                                           @RequestParam(required = false) String productDescription,
                                           @RequestParam(required = false) BigDecimal productPrice,
                                           @RequestParam(required = false) Long categoryId) {
        ResponseEntity<?> authResponse = auth.authorize(authHeader, "manager");
        if (authResponse != null) {
            return authResponse;
        }

        Product product = ProductRepository.updateProduct(productId, productName, productDescription, productPrice, categoryId);
        if (product == null) {
            return ResponseEntity.status(400).body(ResponseDTO.error(400, "FAILED_UPDATE", "Cập nhật không thành công!"));
        }
        return ResponseEntity.ok(ResponseDTO.success("Cập nhật thành công", Map.of("product", product)));
    }

    @GetMapping("/{productId}/details")
    public ResponseEntity<?> getProductDetails(@PathVariable Long productId) {
        ProductDetail productDetail = ProductRepository.getProductDetails(productId);
        if (productDetail == null) {
            return ResponseEntity.status(400).body(ResponseDTO.error(400, "GET_FAILED", "Có lỗi xảy ra, vui lòng thử lại!"));
        }
        return ResponseEntity.ok(ResponseDTO.success("Lấy chi tiết sản phẩm thành công!", Map.of("productDetail", productDetail)));
    }
}


