package com.ureca.yoajungserver.plan.dto.response;

import com.ureca.yoajungserver.plan.entity.Benefit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DetailBenefitDto {
    private String name;
    private String description;
    private Integer voiceLimit;
    private Integer smsLimit;
    private Integer discountAmount;

    public static DetailBenefitDto fromBenefit(Benefit benefit) {
        if (benefit == null) {
            return null;
        }

        return DetailBenefitDto.builder()
                .name(benefit.getName())
                .description(benefit.getDescription())
                .voiceLimit(benefit.getVoiceLimit())
                .smsLimit(benefit.getSmsLimit())
                .discountAmount(benefit.getDiscountAmount())
                .build();
    }
}
