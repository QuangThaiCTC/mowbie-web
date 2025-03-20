package com.mowbie.mowbie_backend.dto;

import lombok.Getter;

@Getter
public class InventoryDTO {
    private Long productId;
    private String productName;
    private Integer stockQuantity;

    public InventoryDTO(Long productId, String productName, Integer stockQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.stockQuantity = stockQuantity;
    }
}
