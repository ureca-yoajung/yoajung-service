package com.ureca.yoajungserver.chatbot.controller;

import static com.ureca.yoajungserver.common.BaseCode.KEYWORD_MAPPING_SUCCESS;

import com.ureca.yoajungserver.chatbot.dto.ChatbotResponse;
import com.ureca.yoajungserver.chatbot.service.ChatbotService;
import com.ureca.yoajungserver.common.ApiResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @GetMapping("/chat")
    public ResponseEntity<ApiResponse<ChatbotResponse>> getPlanRecommendation(
            @RequestParam("input") String input,
            @RequestParam("userId") String userId) throws IOException {
        return ResponseEntity.ok(ApiResponse.of(KEYWORD_MAPPING_SUCCESS, chatbotService.keywordMapper(input, userId)));
    }
}
