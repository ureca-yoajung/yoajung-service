package com.ureca.yoajungserver.plan.service;

import com.ureca.yoajungserver.plan.dto.request.PlanFilterRequest;
import com.ureca.yoajungserver.plan.dto.response.PlanFilterResponse;

import java.util.List;

public interface PlanFilterService {
    List<PlanFilterResponse> searchPlans(PlanFilterRequest filterRequest);
}