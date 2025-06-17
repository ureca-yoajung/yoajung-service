package com.ureca.yoajungserver.chatbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ureca.yoajungserver.chatbot.dto.PersonalPlanRecommendResponse;
import com.ureca.yoajungserver.chatbot.dto.PlanKeywordFirst;
import com.ureca.yoajungserver.chatbot.dto.PlanKeywordResponse;
import com.ureca.yoajungserver.chatbot.dto.PlanKeywordSecond;
import com.ureca.yoajungserver.chatbot.dto.PlanKeywordThird;
import com.ureca.yoajungserver.chatbot.repository.ChatbotRepository;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService {

    private final ChatClient chatClient;
    private final ChatbotRepository chatbotRepository;
    private final LLMAsyncService llmAsyncService;

    @Value("${spring.ai.chat.system-prompt1}")
    private Resource promptRes1;

    @Value("${spring.ai.chat.system-prompt2}")
    private Resource promptRes2;

    @Value("${spring.ai.chat.system-prompt3}")
    private Resource promptRes3;

    private String prompt1;
    private String prompt2;
    private String prompt3;

    @PostConstruct
    public void init() throws IOException {
        prompt1 = new String(promptRes1.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        prompt2 = new String(promptRes2.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        prompt3 = new String(promptRes3.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    @Override
    public List<PersonalPlanRecommendResponse> keywordMapper(String input, String userId) {
        // 1. 각 LLM 호출을 비동기로 시작합니다.
        //    이 메소드들은 호출 즉시 CompletableFuture 객체를 반환하고, 백그라운드 스레드에서 실행됩니다.
        CompletableFuture<PlanKeywordFirst> future1 = llmAsyncService.getLLMResponse(input, userId, prompt1, PlanKeywordFirst.class);
        CompletableFuture<PlanKeywordSecond> future2 = llmAsyncService.getLLMResponse(input, userId, prompt2, PlanKeywordSecond.class);
        CompletableFuture<PlanKeywordThird> future3 = llmAsyncService.getLLMResponse(input, userId, prompt3, PlanKeywordThird.class);

        // 2. 모든 비동기 작업이 완료될 때까지 메인 스레드를 대기시킵니다.
        CompletableFuture.allOf(future1, future2, future3).join();

        try {
            // 3. 각 Future에서 완료된 결과를 추출합니다.
            //    .join()은 예외를 던지지 않지만, .get()은 checked exception을 던집니다.
            //    allOf() 로 이미 완료를 기다렸기 때문에 여기서 join()은 블로킹되지 않습니다.
            PlanKeywordFirst resp1 = future1.join();
            PlanKeywordSecond resp2 = future2.join();
            PlanKeywordThird resp3 = future3.join();

            PlanKeywordResponse planKeywordResponse = new PlanKeywordResponse(
                    resp1.getCategory(),
                    resp1.getPlanTarget(),
                    resp1.getNetworkType(),
                    resp2.getPrice(),
                    resp2.getSpeedAfterLimit(),
                    resp2.getCallAllowance(),
                    resp2.getSmsAllowance(),
                    resp2.getDataAllowance(),
                    resp2.getTetheringSharing(),
                    resp3.getMediaService(),
                    resp3.getPremiumService());

            log.info("planKeyword1 : {}", resp1);
            log.info("planKeyword2 : {}", resp2);
            log.info("planKeyword3 : {}", resp3);
            log.info("planKeywordResponse : {}", planKeywordResponse);

            return chatbotRepository.recommendPlans(planKeywordResponse);
        } catch (Exception e) {
            // 비동기 작업 중 발생한 예외 처리
            log.error("Error during asynchronous LLM call processing", e);
            // 필요에 따라 사용자 정의 예외를 던지거나 다른 방식으로 처리할 수 있습니다.
            throw new RuntimeException("Failed to get response from LLM.", e);
        }
    }

    @Override
    public List<PersonalPlanRecommendResponse> planList(PlanKeywordResponse keywordResponse) {
        return chatbotRepository.recommendPlans(keywordResponse);
    }

    @Async
    public <T> CompletableFuture<T> getLLMResponse(String input, String userId, String prompt, Class<T> responseType) {
        log.info("Executing getLLMResponse on thread: {}", Thread.currentThread().getName());
        T result = chatClient.prompt()
                .system(prompt)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, userId))
                .user(input)
                .call()
                .entity(responseType);
        return CompletableFuture.completedFuture(result);
    }
}