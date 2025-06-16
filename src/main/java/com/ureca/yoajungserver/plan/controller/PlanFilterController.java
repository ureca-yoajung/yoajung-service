package com.ureca.yoajungserver.plan.controller;

import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.common.BaseCode;
import com.ureca.yoajungserver.plan.dto.request.PlanFilterRequest;
import com.ureca.yoajungserver.plan.dto.response.PlanFilterResponse;
import com.ureca.yoajungserver.plan.dto.response.PlanFilterResultResponse;
import com.ureca.yoajungserver.plan.service.PlanFilterService;
import com.ureca.yoajungserver.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanFilterController {

    private final PlanFilterService planFilterService;

    @GetMapping
    public ApiResponse<PlanFilterResultResponse> search(PlanFilterRequest filterRequest) {
        // Return the new result response (plans + totalCount)
        return ApiResponse.of(BaseCode.PLAN_LIST_SUCCESS,
                              planFilterService.searchPlans(filterRequest));
    }
}