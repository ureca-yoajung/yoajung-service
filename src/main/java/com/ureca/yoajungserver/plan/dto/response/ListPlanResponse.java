package com.ureca.yoajungserver.plan.dto.response;

import com.ureca.yoajungserver.plan.entity.Plan;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ListPlanResponse {
    private List<ListPlanDto> plans;
    private Long count;
}
