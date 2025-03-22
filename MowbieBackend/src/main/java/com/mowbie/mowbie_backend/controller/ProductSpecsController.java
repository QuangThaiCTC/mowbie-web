package com.mowbie.mowbie_backend.controller;

import com.mowbie.mowbie_backend.dto.ResponseDTO;
import com.mowbie.mowbie_backend.repository.ProductImageRepository;
import com.mowbie.mowbie_backend.repository.ProductSpecsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/products/{productId}/specs")
public class ProductSpecsController {

    @PostMapping
    public ResponseEntity<?> addSpecs(@PathVariable Long productId, @RequestParam String specTitle, @RequestParam String specContent){
        int result = ProductSpecsRepository.addProductSpec(productId, specTitle, specContent);
        if (result == 1){
            return ResponseEntity.ok(ResponseDTO.success("Thêm thông số thành công!", null));
        }
        return ResponseEntity.status(500).body(ResponseDTO.error(500, "ADD_FAILED", "Thêm thông số không thành công!"));
    }

    @DeleteMapping("/{specId}")
    public ResponseEntity<?> deleteSpecs(@PathVariable Long productId, @PathVariable Long specId){
        int result = ProductSpecsRepository.deleteProductSpec(specId);
        if (result == 0) {
            return ResponseEntity.status(500).body(ResponseDTO.error(500, "DELETE_FAILED", "Xoá thông số không thành công!"));
        }
        return ResponseEntity.ok(ResponseDTO.success("Xoá thông số thành công!", null));
    }
}
