package com.ureca.yoajungserver.chatbot.dto;

import com.ureca.yoajungserver.plan.entity.BenefitType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BenefitEntry {
    private BenefitType benefitType;
    private Integer voiceLimit;
    private Integer smsLimit;
}
