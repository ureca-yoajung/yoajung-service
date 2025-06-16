package com.ureca.yoajungserver.product.dto;

import com.ureca.yoajungserver.plan.entity.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductResponse {
    private final Long id;
    private final String name;
    private final String description;
    private final String productImage;

    @Builder
    private ProductResponse(Long id, String name, String description, String productImage){
        this.id=id; this.name=name; this.description=description; this.productImage=productImage;
    }

    public static ProductResponse from(Product p){
        return ProductResponse.builder()
                .id(p.getId()).name(p.getName())
                .description(p.getDescription())
                .productImage(p.getProductImage()).build();
    }
}