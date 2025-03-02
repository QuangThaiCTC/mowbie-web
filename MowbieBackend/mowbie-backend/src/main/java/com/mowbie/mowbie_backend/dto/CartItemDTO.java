package com.mowbie.mowbie_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CartItemDTO {
    private Long cartId;
    private Long productId;
    private String productName;
    private String productImage;
    private Long quantity;
    private BigDecimal price;
    private LocalDateTime addedAt;

    public CartItemDTO(Long cartId, Long productId, String productName, String productImage, Long quantity, BigDecimal price, LocalDateTime addedAt) {
        this.cartId = cartId;
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.quantity = quantity;
        this.price = price;
        this.addedAt = addedAt;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
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

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }
}
