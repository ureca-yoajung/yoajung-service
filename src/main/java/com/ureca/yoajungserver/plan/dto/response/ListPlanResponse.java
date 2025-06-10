package com.ureca.yoajungserver.plan.dto.response;

import com.ureca.yoajungserver.plan.entity.Plan;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ListPlanResponse {
    private Long planId;
    private String name;
    private String planCategory;
    private String networkType;
    private Integer basePrice;
    private Integer dataAllowance;
    private Integer tetheringSharingAllowance;
    private Integer speedAfterLimit;
    private String description;
    private List<ListProductDto> products;
    private List<ListBenefitDto> benefits;

    public static ListPlanResponse fromPlan(Plan plan, List<ListProductDto> products, List<ListBenefitDto> benefits) {
        return ListPlanResponse.builder()
                .planId(plan.getId())
                .name(plan.getName())
                .planCategory(plan.getPlanCategory().name())
                .networkType(plan.getNetworkType().name())
                .basePrice(plan.getBasePrice())
                .dataAllowance(plan.getDataAllowance())
                .tetheringSharingAllowance(plan.getTetheringSharingAllowance())
                .speedAfterLimit(plan.getSpeedAfterLimit())
                .description(plan.getDescription())
                .products(products)
                .benefits(benefits)
                .build();
    }
}