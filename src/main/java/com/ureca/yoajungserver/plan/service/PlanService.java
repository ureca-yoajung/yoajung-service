package com.ureca.yoajungserver.plan.service;

import com.ureca.yoajungserver.plan.dto.response.*;

import java.util.List;

public interface PlanService {
    DetailPlanResponse getDetailPlan(Long planId);

    DetailPlanProductResponse getDetailPlanProducts(Long planId);

    DetailPlanBenefitResponse getDetailPlanBenefit(Long planId);

    List<PlanNameResponse> getPlanName(String keyword);
}
