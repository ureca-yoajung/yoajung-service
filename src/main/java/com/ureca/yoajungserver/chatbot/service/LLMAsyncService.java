package com.ureca.yoajungserver.chatbot.service;

import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LLMAsyncService {

    private final ChatClient chatClient;

    @Async("taskExecutor") // AsyncConfig에서 정의한 Executor Bean의 이름을 명시해줍니다.
    public <T> CompletableFuture<T> getLLMResponse(String input, String userId, String prompt, Class<T> responseType) {
        log.info("Executing getLLMResponse on thread: {}", Thread.currentThread().getName());
        return CompletableFuture.completedFuture(
                chatClient.prompt()
                        .system(prompt)
                        .options(ChatOptions.builder()
                                .model("gpt-4o")
                                .temperature(0.5)
                                .build())
                        .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, userId))
                        .user(input)
                        .call()
                        .entity(responseType)
        );
    }
}