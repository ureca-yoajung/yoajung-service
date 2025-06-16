package com.ureca.yoajungserver.plan.service;

import com.ureca.yoajungserver.plan.dto.request.PlanFilterRequest;
import com.ureca.yoajungserver.plan.dto.response.PlanFilterResponse;
import com.ureca.yoajungserver.plan.dto.response.PlanFilterResultResponse;

import java.util.List;

public interface PlanFilterService {
    PlanFilterResultResponse searchPlans(PlanFilterRequest filterRequest);
}