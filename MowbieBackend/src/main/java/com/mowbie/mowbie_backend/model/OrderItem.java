package com.mowbie.mowbie_backend.model;

import java.math.BigDecimal;

public class OrderItem {
    private Long orderItemId;
    private Long orderId;
    private Long productId;
    private int orderItemQuantity;
    private BigDecimal orderItemPrice;

    public OrderItem() {}

    public OrderItem(Long orderItemId, Long orderId, Long productId, int orderItemQuantity, BigDecimal orderItemPrice) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.productId = productId;
        this.orderItemQuantity = orderItemQuantity;
        this.orderItemPrice = orderItemPrice;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getOrderItemQuantity() {
        return orderItemQuantity;
    }

    public void setOrderItemQuantity(int orderItemQuantity) {
        this.orderItemQuantity = orderItemQuantity;
    }

    public BigDecimal getOrderItemPrice() {
        return orderItemPrice;
    }

    public void setOrderItemPrice(BigDecimal orderItemPrice) {
        this.orderItemPrice = orderItemPrice;
    }
}
