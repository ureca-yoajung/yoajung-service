package com.ureca.yoajungserver.summary.entity;

import com.ureca.yoajungserver.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "PlanSummary")
public class PlanSummary extends BaseTimeEntity {

    @Id
    @Column(name = "planId")
    private Long planId; // 1:1 매핑

    @Lob
    @Column(name = "summaryText", nullable = false)
    private String summaryText;

    @Column(name = "reviewCountSnapshot", nullable = false)
    private Integer reviewCountSnapshot; // 요약 시점에 사용된 리뷰 개수

    @Builder
    private PlanSummary(Long planId, String summaryText, Integer reviewCountSnapshot) {
        this.planId = planId;
        this.summaryText = summaryText;
        this.reviewCountSnapshot = reviewCountSnapshot;
    }
}