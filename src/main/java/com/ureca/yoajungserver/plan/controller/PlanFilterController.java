package com.ureca.yoajungserver.plan.controller;

import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.common.BaseCode;
import com.ureca.yoajungserver.plan.dto.request.PlanFilterRequest;
import com.ureca.yoajungserver.plan.dto.response.PlanFilterResultResponse;
import com.ureca.yoajungserver.plan.service.PlanFilterService;
import com.ureca.yoajungserver.swagger.api.PlanFilterControllerSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanFilterController implements PlanFilterControllerSwagger {

    private final PlanFilterService planFilterService;

    @GetMapping
    public ApiResponse<PlanFilterResultResponse> search(PlanFilterRequest filterRequest) {
        return ApiResponse.of(BaseCode.PLAN_LIST_SUCCESS,
                planFilterService.searchPlans(filterRequest));
    }
}