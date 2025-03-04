package com.mowbie.mowbie_backend.controller;

import com.mowbie.mowbie_backend.dto.CartItemDTO;
import com.mowbie.mowbie_backend.repository.CartRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/carts")
public class CartController {

    @GetMapping("/{userId}")
    public ResponseEntity<?> getAllCartItems(@PathVariable Long userId) {
        List<CartItemDTO> cartItems = CartRepository.getCartItemsByUserId(userId);
        if (cartItems.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "Giỏ hàng trống!"));
        }
        return ResponseEntity.ok(cartItems);
    }

    @GetMapping("/count/{userId}")
    public ResponseEntity<?> getCartItemCount(@PathVariable Long userId) {
        Long count = CartRepository.getCartItemCount(userId);
        return ResponseEntity.ok(Map.of("total_items", count));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addItemToCart(@RequestParam Long userId, @RequestParam Long productId, @RequestParam Long quantity) {
        if (quantity <= 0) {
            return ResponseEntity.badRequest().body(Map.of("message", "Số lượng sản phẩm không hợp lệ!"));
        }
        int result = CartRepository.addItemToCart(userId, productId, quantity);
        if (result == 1) {
            return ResponseEntity.ok(Map.of("message", "Thêm sản phẩm vào giỏ hàng thành công!"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Không thể thêm sản phẩm vào giỏ hàng!"));
    }

    @PutMapping("/update/{cartId}")
    public ResponseEntity<?> updateCartItem(@PathVariable Long cartId, @RequestParam Long quantity) {
        if (quantity <= 0) {
            return ResponseEntity.badRequest().body(Map.of("message", "Số lượng sản phẩm không hợp lệ!"));
        }
        int result = CartRepository.updateCartItem(cartId, quantity);
        if (result == 1) {
            return ResponseEntity.ok(Map.of("message", "Cập nhật số lượng thành công!"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Cập nhật không thành công!"));
    }

    @DeleteMapping("/delete/{cartId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long cartId) {
        int result = CartRepository.deleteCartItem(cartId);
        if (result == 1) {
            return ResponseEntity.ok(Map.of("message", "Xóa sản phẩm khỏi giỏ hàng thành công!"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Không thể xóa sản phẩm khỏi giỏ hàng!"));
    }
}
