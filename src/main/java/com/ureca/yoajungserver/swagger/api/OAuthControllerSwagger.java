package com.ureca.yoajungserver.swagger.api;

import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.external.kakao.KakaoSignupRequest;
import com.ureca.yoajungserver.swagger.error.ErrorCode400;
import com.ureca.yoajungserver.swagger.error.ErrorCode409;
import com.ureca.yoajungserver.swagger.response.ApiCreatedResponse;
import com.ureca.yoajungserver.swagger.response.ApiSuccessResponse;
import com.ureca.yoajungserver.user.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "카카오 OAuth API", description = "카카오 소셜 로그인 API")
@RequestMapping("/api/oauth/kakao")
public interface OAuthControllerSwagger {

    @Operation(
            summary = "인가 코드로 액세스 토큰 조회",
            description = "카카오 서버로부터 받은 인가 코드를 전달하여 카카오 액세스 토큰 발급"
    )
    @ApiSuccessResponse(description = "액세스 토큰 발급 성공")
    @ErrorCode400(description = "인가 코드가 누락되었거나 유효하지 않음")
    @GetMapping("/token")
    Map<String, String> getAccessToken(@RequestParam("code") String code);

    @Operation(
            summary = "카카오 로그인",
            description = "카카오 액세스 토큰을 사용하여 서비스에 로그인합니다." +
                    "기존 회원은 로그인 신규회원은 추가정보 반환"
    )
    @ApiSuccessResponse(description = "로그인 성공 또는 추가정도 필요")
    @ErrorCode400(description = "액세스 토큰이 유효하지 않음")
    @GetMapping("/login")
    ResponseEntity<ApiResponse<Map<String, Object>>> kakaoLogin(
            @RequestParam("accessToken") String accessToken,
            @Parameter(hidden = true) HttpServletRequest request
    );

    @Operation(
            summary = "카카오 계정으로 회원가입",
            description = "카카오 로그인 시 추가정보가 필요했던 사용자에게 입력받아 회원가입"
    )
    @ApiCreatedResponse(description = "회원가입 및 카카오 로그인 성공")
    @ErrorCode400(description = "추가 정보 입력값이 유효하지않음")
    @ErrorCode409(description = "이미 가입된 이메일")
    @PostMapping("/signup")
    ResponseEntity<ApiResponse<UserResponse>> kakaoSignup(
            @Valid @RequestBody KakaoSignupRequest request,
            @Parameter(hidden = true) HttpServletRequest servletRequest
    );
}
