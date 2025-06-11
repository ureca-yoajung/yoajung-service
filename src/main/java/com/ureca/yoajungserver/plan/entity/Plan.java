package com.ureca.yoajungserver.plan.entity;

import com.ureca.yoajungserver.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE Plan SET deletedAt = NOW() WHERE id = ?")
@SQLRestriction("deletedAt is NULL")
public class Plan extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

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

    @Column
    private LocalDateTime deletedAt;

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "plan", fetch = FetchType.LAZY)
    private List<PlanBenefit> planBenefits;

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "plan", fetch = FetchType.LAZY)
    private List<PlanProduct> planProducts;

    @Builder
    private Plan(String name, NetworkType networkType, PlanCategory planCategory, Integer basePrice, Integer dataAllowance, Integer tetheringSharingAllowance, Integer speedAfterLimit, String description) {
        this.name = name;
        this.networkType = networkType;
        this.planCategory = planCategory;
        this.basePrice = basePrice;
        this.dataAllowance = dataAllowance;
        this.tetheringSharingAllowance = tetheringSharingAllowance;
        this.speedAfterLimit = speedAfterLimit;
        this.description = description;
    }
}
