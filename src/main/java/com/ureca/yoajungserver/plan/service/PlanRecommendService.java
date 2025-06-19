package com.ureca.yoajungserver.plan.service;

import com.ureca.yoajungserver.plan.dto.request.DetailPlanRequest;
import com.ureca.yoajungserver.plan.dto.response.PlanRecommendResponse;
import com.ureca.yoajungserver.user.security.CustomUserDetails;

public interface PlanRecommendService {
    PlanRecommendResponse getRecommendPlan(Long planId, CustomUserDetails userDetails);
}
