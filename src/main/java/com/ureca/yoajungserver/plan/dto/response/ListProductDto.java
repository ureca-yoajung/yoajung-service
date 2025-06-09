package com.ureca.yoajungserver.plan.dto.response;

import com.ureca.yoajungserver.plan.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ListProductDto {
    private String name;
    private String productImage;

    public static ListProductDto fromService(Product product) {
        return ListProductDto.builder()
                .name(product.getName())
                .productImage(product.getProductImage())
                .build();
    }
}
