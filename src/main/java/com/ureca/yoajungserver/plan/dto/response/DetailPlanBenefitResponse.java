package com.ureca.yoajungserver.plan.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DetailPlanBenefitResponse {
    private DetailBenefitDto voice;
    private DetailBenefitDto sms;
    private DetailBenefitDto media;
    private DetailBenefitDto smartDevice;
    private DetailBenefitDto base;
    private DetailBenefitDto premium;
    private DetailBenefitDto other;
}
