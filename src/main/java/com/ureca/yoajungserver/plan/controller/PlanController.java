package com.ureca.yoajungserver.plan.controller;

import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.plan.dto.response.DetailPlanBenefitResponse;
import com.ureca.yoajungserver.plan.dto.response.DetailPlanProductResponse;
import com.ureca.yoajungserver.plan.dto.response.DetailPlanResponse;
import com.ureca.yoajungserver.plan.dto.response.PlanNameResponse;
import com.ureca.yoajungserver.plan.service.PlanService;
import com.ureca.yoajungserver.swagger.api.PlanControllerSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ureca.yoajungserver.common.BaseCode.*;

@RestController
@RequestMapping("api/plan")
@RequiredArgsConstructor
public class PlanController implements PlanControllerSwagger {

    private final PlanService planService;

    @GetMapping("/{planId}")
    public ResponseEntity<ApiResponse<?>> getDetailPlan(@PathVariable(value = "planId") Long planId) {
        DetailPlanResponse responses = planService.getDetailPlan(planId);
        return ResponseEntity
                .status(PLAN_DETAIL_SUCCESS.getStatus())
                .body(ApiResponse.of(PLAN_DETAIL_SUCCESS, responses));
    }

    @GetMapping("/products/{planId}")
    public ResponseEntity<ApiResponse<?>> getDetailPlanProduct(@PathVariable(value = "planId") Long planId) {
        DetailPlanProductResponse responses = planService.getDetailPlanProducts(planId);
        return ResponseEntity
                .status(PLAN_PRODUCT_SUCCESS.getStatus())
                .body(ApiResponse.of(PLAN_PRODUCT_SUCCESS, responses));
    }

    @GetMapping("/benefits/{planId}")
    public ResponseEntity<ApiResponse<?>> getDetailPlanBenefit(@PathVariable(value = "planId") Long planId) {
        DetailPlanBenefitResponse responses = planService.getDetailPlanBenefit(planId);
        return ResponseEntity
                .status(PLAN_BENEFIT_SUCCESS.getStatus())
                .body(ApiResponse.of(PLAN_BENEFIT_SUCCESS, responses));
    }

    @GetMapping("/names")
    public ResponseEntity<ApiResponse<List<PlanNameResponse>>> getPlanNames(
            @RequestParam(required = false) String keyword
    ) {
        List<PlanNameResponse> plans = planService.getPlanName(keyword);
        return ResponseEntity.ok(ApiResponse.of(PLAN_LIST_SUCCESS, plans));
    }
}
