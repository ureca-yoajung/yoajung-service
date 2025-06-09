package com.ureca.yoajungserver.plan.service;

import com.ureca.yoajungserver.plan.dto.response.DetailPlanBenefitResponse;
import com.ureca.yoajungserver.plan.dto.response.DetailPlanProductResponse;
import com.ureca.yoajungserver.plan.dto.response.DetailPlanResponse;
import com.ureca.yoajungserver.plan.dto.response.ListPlanResponse;

import java.util.List;

public interface PlanService {
    List<ListPlanResponse> getListPlan(int page, int size, String planType);
    DetailPlanResponse getDetailPlan(Long planId);
    DetailPlanProductResponse getDetailPlanProducts(Long planId);
    DetailPlanBenefitResponse getDetailPlanBenefit(Long planId);
}
