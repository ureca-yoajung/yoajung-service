package com.ureca.yoajungserver.chatbot.dto;

import com.ureca.yoajungserver.plan.entity.NetworkType;
import com.ureca.yoajungserver.plan.entity.PlanCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalPlanListResponse {
    private Long id;

    private String planName;

    private PlanCategory planCategory;

    private Integer price;

    private NetworkType networkType;

    private Integer dataAllowance;

    private Integer speedAfterLimit;

    private Integer tetheringSharing;

}
