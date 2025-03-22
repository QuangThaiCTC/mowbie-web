package com.mowbie.mowbie_backend.dto;

import lombok.Getter;

@Getter
public class ProductImageDTO {
    private Long productImageId;
    private String productImagePath;

    public ProductImageDTO(Long productImageId, String productImagePath) {
        this.productImageId = productImageId;
        this.productImagePath = productImagePath;
    }
}
