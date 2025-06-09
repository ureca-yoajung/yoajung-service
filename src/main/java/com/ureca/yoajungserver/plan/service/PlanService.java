package com.ureca.yoajungserver.plan.service;

import com.ureca.yoajungserver.plan.dto.response.DetailPlanBenefitResponse;
import com.ureca.yoajungserver.plan.dto.response.DetailPlanProductResponse;
import com.ureca.yoajungserver.plan.dto.response.ListPlanResponse;

import java.util.List;

public interface PlanService {
    List<ListPlanResponse> getListPlan(int page, int size);
    DetailPlanProductResponse getDetailPlanProducts(Long planId);
    DetailPlanBenefitResponse getDetailPlanBenefit(Long planId);
}
