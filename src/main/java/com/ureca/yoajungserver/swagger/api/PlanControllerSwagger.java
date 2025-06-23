package com.ureca.yoajungserver.swagger.api;

import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.plan.dto.response.PlanNameResponse;
import com.ureca.yoajungserver.swagger.response.ApiSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "요금제 API",
        description = "요금제 상세 조회, 상품 혜택 조회, 요금제 검색"
)
@RequestMapping("/api/plan")
public interface PlanControllerSwagger {

    @Operation(
            summary = "요금제 상세 조회",
            description = "요금제의 상세 정보 반환"
    )
    @ApiSuccessResponse(description = "요금제 상세 조회 성공")
    @GetMapping("/{planId}")
    ResponseEntity<ApiResponse<?>> getDetailPlan(
            @PathVariable("planId") Long planId
    );

    @Operation(
            summary = "요금제 상품 목록 조회",
            description = "요금제의 상품 목록 반환"
    )
    @ApiSuccessResponse(description = "요금제 상품 조회 성공")
    @GetMapping("/products/{planId}")
    ResponseEntity<ApiResponse<?>> getDetailPlanProduct(
            @PathVariable("planId") Long planId
    );

    @Operation(
            summary = "요금제 혜택 목록 조회",
            description = "요금제의 혜택 목록을 반환"
    )
    @ApiSuccessResponse(description = "요금제 혜택 조회 성공")
    @GetMapping("/benefits/{planId}")
    ResponseEntity<ApiResponse<?>> getDetailPlanBenefit(
            @PathVariable("planId") Long planId
    );

    @Operation(
            summary = "요금제 이름 검색",
            description = "요금제 이름 목록 반환 (keyword 필터링)"
    )
    @ApiSuccessResponse(description = "요금제 검색 목록 조회 성공")
    @GetMapping("/names")
    ResponseEntity<ApiResponse<List<PlanNameResponse>>> getPlanNames(
            @RequestParam(value = "keyword", required = false) String keyword
    );
}