package com.ureca.yoajungserver.plan.entity;

import com.ureca.yoajungserver.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Benefit extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BenefitType benefitType;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer voiceLimit;

    @Column(nullable = false)
    private Integer smsLimit;

    @Column(nullable = false)
    private Integer discountAmount;

    @OneToMany(mappedBy = "benefit")
    private List<PlanBenefit> planBenefits = new ArrayList<>();

    @Builder
    private Benefit(BenefitType benefitType, String name, String description, Integer voiceLimit, Integer smsLimit, Integer discountAmount) {
        this.benefitType = benefitType;
        this.name = name;
        this.description = description;
        this.voiceLimit = voiceLimit;
        this.smsLimit = smsLimit;
        this.discountAmount = discountAmount;
    }
}
