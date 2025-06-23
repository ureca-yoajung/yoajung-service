package com.ureca.yoajungserver.swagger.api;

import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.plan.dto.response.PlanRecommendResponse;
import com.ureca.yoajungserver.swagger.response.ApiSuccessResponse;
import com.ureca.yoajungserver.user.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "요금제 추천 API",
        description = "사용자 정보, 성향 기반 요금제 추천"
)
@RequestMapping("/recommendation")
public interface PlanRecommendControllerSwagger {

    @Operation(
            summary = "요금제 추천 조회",
            description = "주어진 요금제 ID와 로그인된 사용자 정보를 이용해 추천 요금제를 반환합니다.",
            security = @SecurityRequirement(name = "SessionCookie")
    )
    @ApiSuccessResponse(description = "추천 요금제 조회 성공")
    @GetMapping("/{planId}")
    ResponseEntity<ApiResponse<PlanRecommendResponse>> getRecommendPlan(
            @PathVariable("planId") Long planId,
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails
    );
}