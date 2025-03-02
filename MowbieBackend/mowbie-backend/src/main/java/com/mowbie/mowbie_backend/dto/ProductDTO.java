package com.mowbie.mowbie_backend.dto;

import java.math.BigDecimal;

public class ProductDTO {
    private Long productId;
    private String productName;
    private String productDescription;
    private String productImagePath;
    private BigDecimal productPrice;
    private Long category;

    public ProductDTO(Long productId, String productName, String productDescription, String productImagePath, BigDecimal productPrice, Long category) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productImagePath = productImagePath;
        this.productPrice = productPrice;
        this.category = category;
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

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }
}
