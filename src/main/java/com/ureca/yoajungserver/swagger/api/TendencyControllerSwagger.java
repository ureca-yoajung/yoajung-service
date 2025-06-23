package com.ureca.yoajungserver.swagger.api;

import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.swagger.error.ErrorCode400;
import com.ureca.yoajungserver.swagger.error.ErrorCode401;
import com.ureca.yoajungserver.swagger.response.ApiCreatedResponse;
import com.ureca.yoajungserver.swagger.response.ApiSuccessResponse;
import com.ureca.yoajungserver.user.dto.reqeust.TendencyRequest;
import com.ureca.yoajungserver.user.dto.response.TendencyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "성향 API", description = "사용자 성향 조회, 등록, 수정")
@RequestMapping("/api/tendency")
@SecurityRequirement(name = "SessionCookie")
public interface TendencyControllerSwagger {

    @Operation(
            summary = "내 성향 조회",
            description = "로그인한 사용자의 성향 정보 조회"
    )
    @ApiSuccessResponse(description = "성향 조회 성공")
    @ErrorCode401(description = "인증 필요")
    @GetMapping
    ResponseEntity<ApiResponse<TendencyResponse>> getMyTendency(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails
    );

    @Operation(
            summary = "내 성향 등록",
            description = "로그인한 사용자의 성향 정보 등록"
    )
    @ApiCreatedResponse(description = "성향 등록 성공")
    @ErrorCode400(description = "잘못된 입력값")
    @ErrorCode401(description = "인증 필요")
    @PostMapping
    ResponseEntity<ApiResponse<TendencyResponse>> registerTendency(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody TendencyRequest request
    );

    @Operation(
            summary = "내 성향 수정",
            description = "로그인한 사용자의 성향 정보 수정"
    )
    @ApiSuccessResponse(description = "성향 수정 성공")
    @ErrorCode400(description = "잘못된 입력값")
    @ErrorCode401(description = "인증 필요")
    @PutMapping
    ResponseEntity<ApiResponse<TendencyResponse>> updateTendency(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody TendencyRequest request
    );
}
