package com.ureca.yoajungserver.swagger.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ureca.yoajungserver.chatbot.dto.ChatResponse;
import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.swagger.error.ErrorCode400;
import com.ureca.yoajungserver.swagger.response.ApiSuccessResponse;
import com.ureca.yoajungserver.user.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Tag(name = "챗봇 API",
        description = "사용자에게 질문을 받아 챗봇 응답을 반환, 대화 내역 관리."
)
public interface ChatbotControllerSwagger {

    @Operation(
            summary = "챗봇 질문",
            description = "질문과 게스트 또는 로그인 세션으로 챗봇 응답 반환"
    )
    @ApiSuccessResponse(description = "챗봇 응답 성공")
    @ErrorCode400(description = "잘못된 요청 파라미터")
    @GetMapping("/chat")
    ResponseEntity<ApiResponse<ChatResponse>> getPlanRecommendation(
            @RequestParam("question") String question,
            @RequestParam("guestId") String guestId,
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails user
    ) throws IOException;

    @Operation(
            summary = "선호도 기반 챗봇 질문",
            description = "선호 반영 챗봇 응답 반환",
            security = @SecurityRequirement(name = "SessionCookie")
    )
    @ApiSuccessResponse(description = "챗봇 응답 성공")
    @ErrorCode400(description = "잘못된 요청 파라미터")
    @GetMapping("/chat-preferences")
    ResponseEntity<ApiResponse<ChatResponse>> getPlanRecommendationByPreferences(
            @RequestParam("question") String question,
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails user
    ) throws JsonProcessingException;

    @Operation(
            summary = "챗봇 데화 내역 삭제",
            description = "게스트 또는 사용자의 세션으로 저장된 챗봇 대화 내역 삭제"
    )
    @ApiSuccessResponse(description = "챗봇 히스토리 삭제 성공")
    @ErrorCode400(description = "잘못된 요청 파라미터")
    @DeleteMapping("/chat-clear")
    ResponseEntity<ApiResponse<?>> deleteChatMemory(
            @RequestParam("guestId") String guestId,
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails user
    ) throws IOException;
}