package com.mowbie.mowbie_backend.controller;

import com.mowbie.mowbie_backend.dto.ReviewDTO;
import com.mowbie.mowbie_backend.model.Review;
import com.mowbie.mowbie_backend.repository.ReviewRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getReviewsByProduct(@PathVariable Long productId) {
        List<ReviewDTO> reviews = ReviewRepository.getReviewsByProductId(productId);
        if (reviews.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "Không có đánh giá nào!"));
        }
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addReview(@RequestBody ReviewDTO review) {
        int result = ReviewRepository.addReview(review);
        if (result == 1) {
            return ResponseEntity.ok(Map.of("message", "Thêm đánh giá thành công!"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Thêm đánh giá không thành công!"));
    }

    @PutMapping("/update/{reviewId}")
    public ResponseEntity<?> updateReview(@PathVariable Long reviewId, @RequestParam int rating, @RequestParam String comment) {
        int result = ReviewRepository.updateReview(reviewId, rating, comment);
        if (result == 1) {
            return ResponseEntity.ok(Map.of("message", "Cập nhật đánh giá thành công!"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Cập nhật đánh giá không thành công!"));
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) {
        int result = ReviewRepository.deleteReview(reviewId);
        if (result == 1) {
            return ResponseEntity.ok(Map.of("message", "Xóa đánh giá thành công!"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Xóa đánh giá không thành công!"));
    }
}

