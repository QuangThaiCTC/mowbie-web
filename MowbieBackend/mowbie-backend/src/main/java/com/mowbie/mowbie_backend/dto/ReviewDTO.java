package com.mowbie.mowbie_backend.dto;

public class ReviewDTO {
    private Long reviewId;
    private Long userId;
    private Long productId;
    private int reviewRating;
    private String reviewComment;

    public ReviewDTO(Long reviewId, Long userId, Long productId, int reviewRating, String reviewComment) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.productId = productId;
        this.reviewRating = reviewRating;
        this.reviewComment = reviewComment;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(int reviewRating) {
        this.reviewRating = reviewRating;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }
}
