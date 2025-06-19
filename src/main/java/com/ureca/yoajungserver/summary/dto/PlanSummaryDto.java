package com.ureca.yoajungserver.summary.dto;

import com.ureca.yoajungserver.summary.entity.PlanSummary;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class PlanSummaryDto {

    private final Long planId;
    private final String summaryText;
    private final LocalDateTime updatedAt;

    public static PlanSummaryDto fromEntity(PlanSummary entity) {
        return new PlanSummaryDto(
                entity.getPlanId(),
                entity.getSummaryText(),
                entity.getLastModifiedDate()  // BaseTimeEntity 필드
        );
    }
}