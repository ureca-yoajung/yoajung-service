package com.ureca.yoajungserver.plan.entity;

import com.ureca.yoajungserver.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String productImage;

    @Builder
    private Product(String name, ProductType productType, ProductCategory productCategory, String description, String productImage) {
        this.name = name;
        this.productType = productType;
        this.productCategory = productCategory;
        this.description = description;
        this.productImage = productImage;
    }
}
