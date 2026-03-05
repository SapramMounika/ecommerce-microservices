package com.ecommerce.productservice.dto;

public class ProductDeleteResponse {

    private Long productId;
   

    public ProductDeleteResponse() {
    }

    public ProductDeleteResponse(Long productId, String message) {
        this.productId = productId;
     
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

  
}