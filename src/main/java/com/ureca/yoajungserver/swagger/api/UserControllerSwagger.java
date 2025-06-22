package com.ureca.yoajungserver.swagger.api;

import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.swagger.error.ErrorCode400;
import com.ureca.yoajungserver.swagger.error.ErrorCode401;
import com.ureca.yoajungserver.swagger.error.ErrorCode404;
import com.ureca.yoajungserver.swagger.error.ErrorCode409;
import com.ureca.yoajungserver.swagger.response.ApiCreatedResponse;
import com.ureca.yoajungserver.swagger.response.ApiSuccessResponse;
import com.ureca.yoajungserver.user.dto.reqeust.SignupRequest;
import com.ureca.yoajungserver.user.dto.reqeust.UserUpdateRequest;
import com.ureca.yoajungserver.user.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 API", description = "회원가입, 내정보 조회, 내정보 수정")
@RequestMapping("/api/user")
public interface UserControllerSwagger {

    @Operation(
            summary = "회원가입",
            description = "이메일 인증을 통해 레디스 세션에 VERIFIED_EMAIL을 기반으로 회원가입"
    )
    @ApiCreatedResponse(description = "회원가입 성공")
    @ErrorCode401(description = "이메일 인증이 안됐거나 세션이 만료된 경우")
    @ErrorCode409(description = "이미 가입된 이메일")
    @PostMapping("/signup")
    ResponseEntity<ApiResponse<Void>> signup(
            @Valid @RequestBody SignupRequest request,
            @Parameter(hidden = true) HttpServletRequest servletRequest
    );

    @Operation(
            summary = "내 정보 조회",
            description = "로그인한 사용자의 정보를 조회",
            security = @SecurityRequirement(name = "SessionCookie")
    )
    @ApiSuccessResponse(description = "조회 성공")
    @ErrorCode401(description = "인증 되지않은 사용자")
    @ErrorCode404(description = "사용자 정보가 없습니다")
    @GetMapping("/me")
    ResponseEntity<ApiResponse<UserResponse>> getMyInfo(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails
    );

    @Operation(
            summary = "내 정보 수정",
            description = "로그인한 사용자의 정보를 수정",
            security = @SecurityRequirement(name = "SessionCookie")
    )
    @ApiSuccessResponse(description = "수정 성공")
    @ErrorCode400(description = "입력값이 유효하지 않음")
    @ErrorCode401(description = "인증 되지않은 사용자")
    @ErrorCode404(description = "사용자 정보가 없습니다")
    @PatchMapping("/me")
    ResponseEntity<ApiResponse<UserResponse>> updateMyInfo(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UserUpdateRequest request
    );
}
