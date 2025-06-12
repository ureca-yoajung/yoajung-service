package com.ureca.yoajungserver.chatbot.dto;

import com.ureca.yoajungserver.plan.entity.NetworkType;
import com.ureca.yoajungserver.plan.entity.PlanCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalPlanRecommendResponse {
    private PersonalPlanListResponse response;

    private Integer callAllowance;

    private Integer smsAllowance;
}
