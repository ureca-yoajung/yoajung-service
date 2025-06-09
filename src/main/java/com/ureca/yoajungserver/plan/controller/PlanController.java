package com.ureca.yoajungserver.plan.controller;

import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.plan.dto.response.DetailPlanBenefitResponse;
import com.ureca.yoajungserver.plan.dto.response.DetailProductDto;
import com.ureca.yoajungserver.plan.dto.response.ListPlanResponse;
import com.ureca.yoajungserver.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ureca.yoajungserver.common.BaseCode.*;

@RestController
@RequestMapping("api/plan")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getListPlan(@RequestParam int page, @RequestParam int size) {
        List<ListPlanResponse> responses = planService.getListPlan(page, size);

        return ResponseEntity
                .status(PLAN_LIST_SUCCESS.getStatus())
                .body(ApiResponse.of(PLAN_LIST_SUCCESS, responses));
    }

    @GetMapping("/products/{planId}")
    public ResponseEntity<ApiResponse<?>> getListPlanProduct(@PathVariable(value = "planId") Long planId) {
        List<DetailProductDto> responses = planService.getListPlanProducts(planId);
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
}
