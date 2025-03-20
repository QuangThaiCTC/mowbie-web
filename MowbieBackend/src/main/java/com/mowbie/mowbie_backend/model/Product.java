package com.mowbie.mowbie_backend.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Product {
    private Long productId;
    private String productName;
    private String productDescription;
    private BigDecimal productPrice;
    private Long categoryId;

    public Product(Long productId, String productName, String productDescription, BigDecimal productPrice, Long categoryId) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.categoryId = categoryId;
    }
}
