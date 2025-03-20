package com.mowbie.mowbie_backend.model;

import lombok.Getter;

@Getter
public class ProductImage {
    private Long productImageId;
    private Long productId;
    private String productImagePath;

    public ProductImage(Long productImageId, Long productId, String productImagePath) {
        this.productImageId = productImageId;
        this.productId = productId;
        this.productImagePath = productImagePath;
    }
}
