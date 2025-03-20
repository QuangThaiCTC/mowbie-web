package com.mowbie.mowbie_backend.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Inventory {
    private Long inventoryId;
    private Long productId;
    private Long stockChange;
    private String changeType;
    private LocalDateTime changeDate;

    public Inventory(Long inventoryId, Long productId, Long stockChange, String changeType, LocalDateTime changeDate) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.stockChange = stockChange;
        this.changeType = changeType;
        this.changeDate = changeDate;
    }
}
