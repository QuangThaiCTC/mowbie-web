package com.mowbie.mowbie_backend.dto;

import lombok.Getter;

@Getter
public class ProductSpecDTO {
    private String title;
    private String content;

    public ProductSpecDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
