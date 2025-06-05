package com.ureca.yoajungserver.plan.entity;

import com.ureca.yoajungserver.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Service extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ServiceCategory serviceCategory;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String serviceImage;

    @Builder
    private Service(String name, ServiceType serviceType, ServiceCategory serviceCategory, String description, String serviceImage) {
        this.name = name;
        this.serviceType = serviceType;
        this.serviceCategory = serviceCategory;
        this.description = description;
        this.serviceImage = serviceImage;
    }
}
