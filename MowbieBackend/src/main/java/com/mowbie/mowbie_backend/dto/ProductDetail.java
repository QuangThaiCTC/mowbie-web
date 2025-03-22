package com.mowbie.mowbie_backend.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class ProductDetail {
    private Long productId;
    private String productName;
    private String productDescription;
    private BigDecimal productPrice;
    private List<ProductImageDTO> productImages;
    private List<ProductSpecDTO> productSpecs;
    private Integer stockQuantity;

    public ProductDetail(Long productId, String productName, String productDescription, BigDecimal productPrice, List<ProductImageDTO> productImages, List<ProductSpecDTO> productSpecs, Integer stockQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productImages = productImages;
        this.productSpecs = productSpecs;
        this.stockQuantity = stockQuantity;
    }
}
