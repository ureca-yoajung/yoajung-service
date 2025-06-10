package com.ureca.yoajungserver.plan.service;

import com.ureca.yoajungserver.plan.dto.response.DetailPlanBenefitResponse;
import com.ureca.yoajungserver.plan.dto.response.DetailPlanProductResponse;
import com.ureca.yoajungserver.plan.dto.response.DetailPlanResponse;
import com.ureca.yoajungserver.plan.dto.response.ListPlanResponse;
import com.ureca.yoajungserver.plan.entity.PlanCategory;
import com.ureca.yoajungserver.plan.entity.PlanSortType;

import java.util.List;

public interface PlanService {
    List<ListPlanResponse> getListPlan(int page, int size, PlanCategory planCategory, PlanSortType sortedType);
    List<ListPlanResponse> getPopularPlans(int page, int size, PlanCategory planCategory, PlanSortType sortedType);
    DetailPlanResponse getDetailPlan(Long planId);
    DetailPlanProductResponse getDetailPlanProducts(Long planId);
    DetailPlanBenefitResponse getDetailPlanBenefit(Long planId);
}
