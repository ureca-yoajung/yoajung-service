package com.ureca.yoajungserver.chatbot.controller;

import com.ureca.yoajungserver.chatbot.dto.PersonalPlanRecommendResponse;
import com.ureca.yoajungserver.chatbot.dto.PlanKeywordResponse;
import com.ureca.yoajungserver.chatbot.service.ChatbotService;
import com.ureca.yoajungserver.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ureca.yoajungserver.common.BaseCode.STATUS_OK;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final ChatClient chatClient;
    private final ChatbotService chatbotService;

    @GetMapping("/test")
    public String testBadWord(@RequestParam("question") String question, @RequestParam("userId") Long userId) {
        return chatClient.prompt().user(question)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, userId))
                .call()
                .content();
    }
}
