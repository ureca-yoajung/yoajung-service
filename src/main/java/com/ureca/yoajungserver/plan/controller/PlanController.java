package com.ureca.yoajungserver.plan.controller;

import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.plan.dto.response.DetailProductDto;
import com.ureca.yoajungserver.plan.dto.response.ListPlanResponse;
import com.ureca.yoajungserver.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ureca.yoajungserver.common.BaseCode.PLAN_LIST_SUCCESS;
import static com.ureca.yoajungserver.common.BaseCode.PLAN_PRODUCT_SUCCESS;

import java.util.List;

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
}
