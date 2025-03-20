package com.mowbie.mowbie_backend.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProductDTO {
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private String productImage;

    public ProductDTO(Long productId, String productName, BigDecimal productPrice, String productImage) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImage = productImage;
    }
}
