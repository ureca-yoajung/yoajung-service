package com.ureca.yoajungserver.plan.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PlanFilterResultResponse {
    private final List<PlanFilterResponse> plans;
    private final long totalCount;
}