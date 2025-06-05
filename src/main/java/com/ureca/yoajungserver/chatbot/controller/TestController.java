package com.ureca.yoajungserver.chatbot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final ChatClient chatClient;

    @GetMapping("/test")
    public String testBadWord(@RequestParam("question") String question, @RequestParam("userId") Long userId) {
        return chatClient.prompt().user(question)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, userId))
                .call()
                .content();
    }
}
