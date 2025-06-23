package com.ureca.yoajungserver.swagger.api;

import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.plan.dto.request.PlanFilterRequest;
import com.ureca.yoajungserver.plan.dto.response.PlanFilterResultResponse;
import com.ureca.yoajungserver.swagger.response.ApiSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "요금제 필터 API",
        description = "요금제 목록 조건 필터링."
)
@RequestMapping("/api/plans")
public interface PlanFilterControllerSwagger {

    @Operation(
            summary = "요금제 필터",
            description = "요금제 필터 검색"
    )
    @ApiSuccessResponse(description = "요금제 필터 검색 성공")
    @GetMapping
    ApiResponse<PlanFilterResultResponse> search(PlanFilterRequest filterRequest);
}