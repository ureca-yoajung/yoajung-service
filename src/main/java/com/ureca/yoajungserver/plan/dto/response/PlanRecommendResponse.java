package com.ureca.yoajungserver.plan.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PlanRecommendResponse {
    private String recommendation;
    private String message;
    private String value;
}
