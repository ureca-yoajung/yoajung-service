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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;
    private final ChatbotRepository chatbotRepository;

    // application.yml 의 키와 일치시킵니다.
    @Value("${spring.ai.chat.system-prompt1}")
    private Resource promptRes1;

    @Value("${spring.ai.chat.system-prompt2}")
    private Resource promptRes2;

    @Value("${spring.ai.chat.system-prompt3}")
    private Resource promptRes3;

    // 실제 사용할 문자열로 변환해 두는 필드
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
    public List<PersonalPlanRecommendResponse> keywordMapper(String input, String userId) throws IOException {
        // 서로 다른 system prompt 를 차례로 사용
        PlanKeywordFirst resp1 = getLLMResponse(input, userId, prompt1, PlanKeywordFirst.class);

        PlanKeywordSecond resp2 = getLLMResponse(input, userId, prompt2, PlanKeywordSecond.class);

        PlanKeywordThird resp3 = getLLMResponse(input, userId, prompt3, PlanKeywordThird.class);

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

        // 예시로 첫 번째 응답을 DB 조회에 사용
        return chatbotRepository.recommendPlans(planKeywordResponse);
    }

    // 요금제 조회
    @Override
    public List<PersonalPlanRecommendResponse> planList(PlanKeywordResponse keywordResponse) {
        return chatbotRepository.recommendPlans(keywordResponse);
    }

    private <T> T getLLMResponse(String input, String userId, String prompt, Class<T> responseType) {
        return chatClient.prompt()
                .system(prompt)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, userId))
                .user(input)
                .call()
                .entity(responseType);
    }
}
