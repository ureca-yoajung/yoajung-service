package com.ureca.yoajungserver.plan.entity;

import com.ureca.yoajungserver.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Plan extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NetworkType networkType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PlanCategory planCategory;

    @Column(nullable = false)
    private Integer basePrice;

    @Column(nullable = false)
    private Integer dataAllowance;

    @Column(nullable = false)
    private Integer tetheringSharingAllowance;

    @Column(nullable = false)
    private Integer speedAfterLimit;

    @Column(nullable = false)
    private String description;

    @Builder
    private Plan(NetworkType networkType, PlanCategory planCategory, Integer basePrice, Integer dataAllowance, Integer tetheringSharingAllowance, Integer speedAfterLimit, String description) {
        this.networkType = networkType;
        this.planCategory = planCategory;
        this.basePrice = basePrice;
        this.dataAllowance = dataAllowance;
        this.tetheringSharingAllowance = tetheringSharingAllowance;
        this.speedAfterLimit = speedAfterLimit;
        this.description = description;
    }
}
