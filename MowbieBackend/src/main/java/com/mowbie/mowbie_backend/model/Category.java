package com.mowbie.mowbie_backend.model;

import lombok.Getter;

@Getter
public class Category {
    private Long categoryId;
    private String categoryName;

    public Category(Long categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}
