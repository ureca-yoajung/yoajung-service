package com.ureca.yoajungserver.plan.dto.response;

import com.ureca.yoajungserver.plan.entity.Plan;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DetailPlanResponse {
    private Long planId;
    private String name;
    private String planCategory;
    private String networkType;
    private Integer basePrice;
    private Integer dataAllowance;
    private Integer tetheringSharingAllowance;
    private Integer speedAfterLimit;
    private String description;
    private String planTarget;

    public static DetailPlanResponse fromPlan(Plan plan) {
        return DetailPlanResponse.builder()
                .planId(plan.getId())
                .name(plan.getName())
                .planCategory(plan.getPlanCategory().name())
                .networkType(plan.getNetworkType().name())
                .basePrice(plan.getBasePrice())
                .dataAllowance(plan.getDataAllowance())
                .tetheringSharingAllowance(plan.getTetheringSharingAllowance())
                .speedAfterLimit(plan.getSpeedAfterLimit())
                .description(plan.getDescription())
                .planTarget(plan.getPlanTarget().name())
                .build();
    }
}
