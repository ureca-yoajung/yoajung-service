package com.ureca.yoajungserver.plan.service;

import com.ureca.yoajungserver.plan.dto.response.ListPlanResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlanService {
    List<ListPlanResponse> getListPlan(int page, int size);
}
