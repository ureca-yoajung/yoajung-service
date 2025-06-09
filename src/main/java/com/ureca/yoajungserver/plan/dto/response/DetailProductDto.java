package com.ureca.yoajungserver.plan.dto.response;

import com.ureca.yoajungserver.plan.entity.Product;
import com.ureca.yoajungserver.plan.entity.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DetailProductDto {
    private String name;
    private String description;
    private String productImage;
    private ProductCategory productCategory;

    public static DetailProductDto fromProduct(Product product) {
        return DetailProductDto.builder()
                .name(product.getName())
                .description(product.getDescription())
                .productImage(product.getProductImage())
                .productCategory(product.getProductCategory())
                .build();
    }
}
