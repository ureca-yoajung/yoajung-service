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
@Table(name="benefit")
public class Benefit extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="benefitType", nullable = false)
    @Enumerated(EnumType.STRING)
    private BenefitType benefitType;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name="voiceLimit")
    private Integer voiceLimit;

    @Column(name="smsLimit")
    private Integer smsLimit;

    @Column(name="discountAmount")
    private Integer discountAmount;

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
