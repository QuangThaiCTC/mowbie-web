package com.mowbie.mowbie_backend.model;

import lombok.Getter;

@Getter
public class ProductSpec {
    private Long productSpecId;
    private Long productId;
    private String specTitle;
    private String specContent;

    public ProductSpec(Long productSpecId, Long productId, String specTitle, String specContent) {
        this.productSpecId = productSpecId;
        this.productId = productId;
        this.specTitle = specTitle;
        this.specContent = specContent;
    }
}
