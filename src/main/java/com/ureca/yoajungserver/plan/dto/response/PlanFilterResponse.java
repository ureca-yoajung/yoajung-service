package com.ureca.yoajungserver.plan.dto.response;

import com.ureca.yoajungserver.plan.entity.Plan;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class PlanFilterResponse {

    @Getter
    @Builder
    public static class BenefitSummary {
        private String name;
        private String description;
    }

    private Long id;
    private String name;
    private Integer basePrice;
    private Integer dataAllowance;
    private Integer speedAfterLimit;
    private Set<String> productNames;
    private List<BenefitSummary> benefits;

    public static PlanFilterResponse from(Plan plan) {
        return PlanFilterResponse.builder()
                .id(plan.getId())
                .name(plan.getName())
                .basePrice(plan.getBasePrice())
                .dataAllowance(plan.getDataAllowance())
                .speedAfterLimit(plan.getSpeedAfterLimit())
                .productNames(
                        plan.getPlanProducts().stream()
                            .map(pp -> pp.getProduct().getName())
                            .collect(Collectors.toSet()))
                .benefits(
                        plan.getPlanBenefits().stream()
                            .map(pb -> BenefitSummary.builder()
                                    .name(pb.getBenefit().getName())
                                    .description(pb.getBenefit().getDescription())
                                    .build())
                            .toList())
                .build();
    }
}
