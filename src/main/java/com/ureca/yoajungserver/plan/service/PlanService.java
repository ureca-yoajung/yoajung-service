package com.ureca.yoajungserver.plan.service;

import com.ureca.yoajungserver.plan.dto.response.*;
import com.ureca.yoajungserver.plan.entity.PlanCategory;
import com.ureca.yoajungserver.plan.entity.PlanSortType;

import java.util.List;

public interface PlanService {
    ListPlanResponse getListPlan(int page, int size, PlanCategory planCategory, PlanSortType sortedType);

    ListPlanResponse getPopularPlans(int page, int size, PlanCategory planCategory, PlanSortType sortedType);

    DetailPlanResponse getDetailPlan(Long planId);

    DetailPlanProductResponse getDetailPlanProducts(Long planId);

    DetailPlanBenefitResponse getDetailPlanBenefit(Long planId);

    List<PlanNameResponse> getPlanName(String keyword);
}
