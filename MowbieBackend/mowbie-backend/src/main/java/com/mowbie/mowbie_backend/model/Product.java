package com.mowbie.mowbie_backend.model;

import java.math.BigDecimal;

public class Product {
    private Long productId;
    private String productName;
    private String productDescription;
    private String productImagePath;
    private BigDecimal productPrice;
    private Long categoryId;

    public Product() {}

    public Product(Long productId, String productName, String productDescription, String productImagePath, BigDecimal productPrice, Long categoryId) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productImagePath = productImagePath;
        this.productPrice = productPrice;
        this.categoryId = categoryId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductImagePath() {
        return productImagePath;
    }

    public void setProductImagePath(String productImagePath) {
        this.productImagePath = productImagePath;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
