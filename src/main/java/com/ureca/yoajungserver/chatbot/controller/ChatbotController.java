package com.ureca.yoajungserver.chatbot.controller;

import static com.ureca.yoajungserver.common.BaseCode.KEYWORD_MAPPING_SUCCESS;
import static com.ureca.yoajungserver.common.BaseCode.STATUS_OK;

import com.ureca.yoajungserver.chatbot.dto.PlanKeywordResponse;
import com.ureca.yoajungserver.chatbot.dto.PersonalPlanRecommendResponse;
import com.ureca.yoajungserver.chatbot.service.ChatbotService;
import com.ureca.yoajungserver.common.ApiResponse;
import java.io.IOException;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @GetMapping("/chat")
    public ResponseEntity<ApiResponse<List<PersonalPlanRecommendResponse>>> getPlanRecommendation(
            @RequestParam("input") String input,
            @RequestParam("userId") String userId) throws IOException {
        return ResponseEntity.ok(ApiResponse.of(KEYWORD_MAPPING_SUCCESS, chatbotService.keywordMapper(input, userId)));
    }

}
