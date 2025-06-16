package com.ureca.yoajungserver.plan.entity;

import com.ureca.yoajungserver.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "plan")
@SQLDelete(sql = "UPDATE plan SET deletedAt = NOW() WHERE id = ?")
@SQLRestriction("deletedAt is NULL")
public class Plan extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "networkType", nullable = false)
    @Enumerated(EnumType.STRING)
    private NetworkType networkType;

    @Column(name = "planTarget", nullable = false)
    @Enumerated(EnumType.STRING)
    private PlanTarget planTarget;

    @Column(name = "planCategory", nullable = false)
    @Enumerated(EnumType.STRING)
    private PlanCategory planCategory;

    @Column(name = "basePrice", nullable = false)
    private Integer basePrice;

    @Column(name = "dataAllowance", nullable = false)
    private Integer dataAllowance;

    @Column(name = "tetheringSharingAllowance", nullable = false)
    private Integer tetheringSharingAllowance;

    @Column(name = "speedAfterLimit", nullable = false)
    private Integer speedAfterLimit;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "deletedAt")
    private LocalDateTime deletedAt;

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "plan", fetch = FetchType.LAZY)
    private List<PlanBenefit> planBenefits;

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "plan", fetch = FetchType.LAZY)
    private List<PlanProduct> planProducts;

    @Builder
    private Plan(String name, NetworkType networkType, PlanCategory planCategory, Integer basePrice, Integer dataAllowance, Integer tetheringSharingAllowance,
                 Integer speedAfterLimit, String description) {
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
