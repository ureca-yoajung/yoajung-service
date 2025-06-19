package com.ureca.yoajungserver.chatbot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ureca.yoajungserver.chatbot.dto.ChatResponse;
import com.ureca.yoajungserver.chatbot.service.ChatbotService;
import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.user.security.CustomUserDetails;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.ureca.yoajungserver.common.BaseCode.*;

@RestController
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @GetMapping("/chat")
    public ResponseEntity<ApiResponse<ChatResponse>> getPlanRecommendation(
            @RequestParam("question") String question,
            @RequestParam("guestId") String guestId,
            @AuthenticationPrincipal CustomUserDetails user) throws IOException {
        String id = (user != null && user.getId() != null)
                ? user.getId().toString()
                : guestId;
        return ResponseEntity.ok(ApiResponse.of(KEYWORD_MAPPING_SUCCESS, chatbotService.keywordMapper(question, id)));
    }

    @GetMapping("/chat-preferences")
    public ResponseEntity<ApiResponse<ChatResponse>> getPlanRecommendationByPreferences(
            @RequestParam("question") String question,
            @AuthenticationPrincipal CustomUserDetails user) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse
                .of(KEYWORD_MAPPING_SUCCESS, chatbotService.keywordMapperByPreferences(question, user.getId())));
    }

    @DeleteMapping("/chat-clear")
    public ResponseEntity<ApiResponse<?>> deleteChatMemory(
            @RequestParam("guestId") String guestId,
            @AuthenticationPrincipal CustomUserDetails user) throws IOException {
        String id = (user != null && user.getId() != null)
                ? user.getId().toString()
                : guestId;
        chatbotService.deleteChatMemory(id);
        return ResponseEntity.ok(ApiResponse.ok(CHAT_DELETE_SUCCESS));
    }
}
