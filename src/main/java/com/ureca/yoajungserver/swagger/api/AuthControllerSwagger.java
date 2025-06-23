package com.ureca.yoajungserver.swagger.api;

import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.swagger.error.ErrorCode400;
import com.ureca.yoajungserver.swagger.error.ErrorCode401;
import com.ureca.yoajungserver.swagger.response.ApiSuccessResponse;
import com.ureca.yoajungserver.user.dto.reqeust.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "인증 API", description = "이메일 인증, 로그인, 로그아웃, 비밀번호 재설정 API")
@RequestMapping("/api/auth")
public interface AuthControllerSwagger {

    @Operation(
            summary = "이메일 인증코드 발송",
            description = "입력한 이메일로 6자리 인종코드를 발송하고 세션에 PENDING_EMAIL 속성 저장")
    @ApiSuccessResponse(description = "인증 코드 발송 성공")
    @ErrorCode400(description = "잘못된 이메일 형식")
    @PostMapping("/send-code")
    ResponseEntity<ApiResponse<Void>> sendCode(
            @Valid @RequestBody SendCodeRequest request,
            @Parameter(hidden = true) HttpServletRequest servletRequest
    );

    @Operation(
            summary = "이메일 인증코드 검증",
            description = "인증코드를 검증하고 성공 시 세션에 VERIFIED_EMAIL 속성 저장")
    @ApiSuccessResponse(description = "인증 성공")
    @ErrorCode400(description = "인증코드 불일치 또는 세션(PENDING_EMAIL) 만료")
    @PostMapping("/verify-code")
    ResponseEntity<ApiResponse<Void>> verifyCode(
            @Valid @RequestBody VerifyCodeRequest request,
            @Parameter(hidden = true) HttpServletRequest servletRequest
    );

    @Operation(
            summary = "로그인"
            , description = "이메일 비밀번호 제출하여 세션기반 인증 수행 JSESSIONID_USER 쿠키 발급")
    @ApiSuccessResponse(description = "로그인 성공")
    @ErrorCode401(description = "이메일 또는 비밀번호 불일치")
    @PostMapping("/login")
    ResponseEntity<ApiResponse<Void>> login(
            @Valid @RequestBody LoginRequest request,
            @Parameter(hidden = true) HttpServletRequest servletRequest
    );

    @Operation(
            summary = "로그아웃",
            description = "세션을 무효화하는 로그아웃",
            security = @SecurityRequirement(name = "SessionCookie")
    )
    @ApiSuccessResponse(description = "로그아웃 성공")
    @PostMapping("/logout")
    ResponseEntity<ApiResponse<Void>> logout(
            @Parameter(hidden = true) HttpSession session);

    @Operation(
            summary = "비밀번호 재설정 링크 발송",
            description = "이메일을 입력받아 재설정 토큰을 생성해서 링크를 메일로 발송")
    @ApiSuccessResponse(description = "링크 발송 성공")
    @ErrorCode400(description = "잘못된 이메일 형식")
    @PostMapping("/reset-request")
    ResponseEntity<ApiResponse<Void>> requestPasswordReset(
            @Valid @RequestBody PasswordResetRequest request);

    @Operation(
            summary = "비밀번호 재설정",
            description = "토큰과 새 비밀번호를 입력받아 사용자 비밀번호를 변경")
    @ApiSuccessResponse(description = "비밀번호 재설정 성공")
    @ErrorCode400(description = "토큰이 유효하지않거나 만료, 비밀번호 유효성 실패")
    @PostMapping("/reset")
    ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody PasswordResetConfirmRequest request);
}
