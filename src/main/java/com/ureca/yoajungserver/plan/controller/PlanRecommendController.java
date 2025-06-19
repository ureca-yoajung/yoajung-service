package com.ureca.yoajungserver.plan.controller;

import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.common.BaseCode;
import com.ureca.yoajungserver.plan.dto.request.DetailPlanRequest;
import com.ureca.yoajungserver.plan.dto.response.PlanRecommendResponse;
import com.ureca.yoajungserver.plan.service.PlanRecommendService;
import com.ureca.yoajungserver.user.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PlanRecommendController {

    private final PlanRecommendService planRecommendService;

    @GetMapping("/recommendation/{planId}")
    public ResponseEntity<ApiResponse<PlanRecommendResponse>> getRecommendPlan(
            @PathVariable(name = "planId")Long planId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity
                .ok(ApiResponse.of(BaseCode.STATUS_OK, planRecommendService.getRecommendPlan(planId, userDetails)));
    }
}
