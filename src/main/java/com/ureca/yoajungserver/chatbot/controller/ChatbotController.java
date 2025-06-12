package com.ureca.yoajungserver.chatbot.controller;

import static com.ureca.yoajungserver.common.BaseCode.KEYWORD_MAPPING_SUCCESS;

import com.ureca.yoajungserver.chatbot.service.ChatbotService;
import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.user.security.CustomUserDetails;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @GetMapping("/chat")
    public ResponseEntity<ApiResponse<?>> getPlanRecommendation(
            @RequestParam("question") String question,
            @RequestParam("guestId") String guestId,
            @AuthenticationPrincipal CustomUserDetails user) throws IOException {
        String id = (user != null && user.getId() != null)
                ? user.getId().toString()
                : guestId;
        return ResponseEntity.ok(ApiResponse.of(KEYWORD_MAPPING_SUCCESS, chatbotService.keywordMapper(question, id)));
    }
}
