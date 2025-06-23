package com.ureca.yoajungserver.swagger.api;


import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.summary.dto.PlanSummaryDto;
import com.ureca.yoajungserver.swagger.response.ApiNoContentResponse;
import com.ureca.yoajungserver.swagger.response.ApiSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "요금제 리뷰 요약 API", description = "특정 요금제 리뷰 요약 조회")
@RequestMapping("/api/plans/{id}/summary")
public interface PlanSummaryControllerSwagger {

    @Operation(
            summary = "요금제 리뷰 요약 조회",
            description = "주어진 플랜 ID의 리뷰에 대한 요약 정보를 반환"
    )
    @ApiSuccessResponse(description = "리뷰 요약 조회 성공")
    @ApiNoContentResponse(description = "조회된 요약 정보가 없음")
    @GetMapping
    ResponseEntity<ApiResponse<PlanSummaryDto>> getPlanSummary(
            @PathVariable Long id);
}