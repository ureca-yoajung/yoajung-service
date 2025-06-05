package com.ureca.yoajungserver.plan.dto.response;

import com.ureca.yoajungserver.plan.entity.Benefit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ListBenefitDto {
    private String name;
    private String description;

    public static ListBenefitDto fromBenefit(Benefit benefit) {
        return ListBenefitDto.builder().
                name(benefit.getName()).
                description(benefit.getDescription())
                .build();
    }
}