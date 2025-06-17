package com.ureca.yoajungserver.plan.dto.response;

import com.ureca.yoajungserver.plan.entity.Plan;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlanNameResponse {
    private Long planId;
    private String name;

    public static PlanNameResponse fromEntity(Plan plan) {
        return new PlanNameResponse(plan.getId(), plan.getName());
    }
}
