package com.ureca.yoajungserver.plan.entity;

import com.ureca.yoajungserver.common.BaseTimeEntity;
import com.ureca.yoajungserver.user.entity.AgeGroup;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "planStatistic")
public class PlanStatistic extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "ageGroup", nullable = false)
    private AgeGroup ageGroup;

    @Enumerated(EnumType.STRING)
    @Column(name = "planCategory", nullable = false)
    private PlanCategory planCategory;

    @Column(name = "planId",nullable = false)
    private Long planId;

    @Column(name = "userCount", nullable = false)
    private Long userCount;

    @Builder
    private PlanStatistic(AgeGroup ageGroup, PlanCategory planCategory, Long planId, Long userCount) {
        this.ageGroup = ageGroup;
        this.planCategory = planCategory;
        this.planId = planId;
        this.userCount = userCount;
    }
}
