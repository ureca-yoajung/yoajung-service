package com.ureca.yoajungserver.plan.entity;

import com.ureca.yoajungserver.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="product")
public class Product extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name="productType", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Column(name="productCategory", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name="productImage", nullable = false)
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
