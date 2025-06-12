package com.ureca.yoajungserver.chatbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ureca.yoajungserver.chatbot.dto.PlanKeywordResponse;
import java.io.IOException;
import java.util.List;

import com.ureca.yoajungserver.chatbot.dto.PersonalPlanRecommendResponse;
import com.ureca.yoajungserver.chatbot.repository.ChatbotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;
    private final ChatbotRepository chatbotRepository;

    @Override
    public List<PersonalPlanRecommendResponse> keywordMapper(String input, String userId) throws IOException {
        // 실제 파싱 시도
        PlanKeywordResponse planKeywordResponse = chatClient.prompt()
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, userId))
                .user(input)
                .call()
                .entity(PlanKeywordResponse.class);

        System.out.println(planKeywordResponse);
        // 필수 필드 검증
        return chatbotRepository.recommendPlans(planKeywordResponse);
    }

    private void checkJsonForm(String trimmed) {
        if (!(trimmed.startsWith("{") && trimmed.endsWith("}"))) {
            throw new IllegalStateException("JSON 형식 오류");
        }
    }

    private PlanKeywordResponse parseLlmOutput(String llmOutput) throws IOException {
        PlanKeywordResponse planKeywordResponse;
        try {
            planKeywordResponse = objectMapper.readValue(llmOutput, PlanKeywordResponse.class);
        } catch (IOException e) {
            throw new IOException("JSON 매핑 실패");
        }
        return planKeywordResponse;
    }

    // 요금제 조회
    @Override
    public List<PersonalPlanRecommendResponse> planList(PlanKeywordResponse keywordResponse) {

        return chatbotRepository.recommendPlans(keywordResponse);
    }
}
