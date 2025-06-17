package com.ureca.yoajungserver.chatbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ureca.yoajungserver.chatbot.dto.*;
import com.ureca.yoajungserver.chatbot.repository.ChatbotRepository;
import com.ureca.yoajungserver.user.entity.Tendency;
import com.ureca.yoajungserver.user.entity.User;
import com.ureca.yoajungserver.user.exception.TendencyNotFoundException;
import com.ureca.yoajungserver.user.exception.UserNotFoundException;
import com.ureca.yoajungserver.user.repository.TendencyRepository;
import com.ureca.yoajungserver.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;
    private final ChatbotRepository chatbotRepository;
    private final UserRepository userRepository;
    private final TendencyRepository tendencyRepository;

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
        prompt1 = readPrompt(promptRes1);
        prompt2 = readPrompt(promptRes2);
        prompt3 = readPrompt(promptRes3);
    }

    private String readPrompt(Resource resource) throws IOException {
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    @Override
    public List<PersonalPlanRecommendResponse> keywordMapper(String question, String userId) throws IOException {
        PlanKeywordResponse planKeywordResponse = getKeyWordResponse(question, Long.parseLong(userId));

        return chatbotRepository.recommendPlans(planKeywordResponse);
    }

    @Override
    public List<PersonalPlanRecommendResponse> keywordMapperByPreferences(String question, Long userId) {
        PlanKeywordResponse planKeywordResponse = getKeyWordResponse(question, userId);

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Tendency tendency = tendencyRepository.findByUser(user).orElseThrow(TendencyNotFoundException::new);

        List<PersonalPlanRecommendResponse> personalPlanRecommendResponses = chatbotRepository.recommendPlans(planKeywordResponse);


        int avgMonthlyDataGB = tendency.getAvgMonthlyDataGB();
        int preferencePrice = tendency.getPreferencePrice();

        // 예시로 첫 번째 응답을 DB 조회에 사용
        List<PlanScore> planScores = new ArrayList<>();

        double weightGB = 0.5;
        double weightPrice = 0.5;

        for (PersonalPlanRecommendResponse personalPlanRecommendResponse : personalPlanRecommendResponses) {
            double dataScore;

            int tempData;

            if (personalPlanRecommendResponse.getResponse().getDataAllowance() == 9999) {
                tempData = 300;
            } else {
                tempData = personalPlanRecommendResponse.getResponse().getDataAllowance();
            }

            dataScore = 1 - (Math.abs(tempData - avgMonthlyDataGB) / (double) avgMonthlyDataGB);
            double priceScore = 1 - (Math.abs(personalPlanRecommendResponse.getResponse().getPrice() - preferencePrice) / (double) preferencePrice);
            dataScore = Math.max(dataScore, 0);
            priceScore = Math.max(priceScore, 0);

            double totalScore = dataScore * weightGB + priceScore * weightPrice;
            planScores.add(new PlanScore(personalPlanRecommendResponse, totalScore));
        }

        planScores.sort(Comparator.comparingDouble(PlanScore::getScore).reversed());

        int limit = Math.min(3, planScores.size());
        List<PersonalPlanRecommendResponse> result = new ArrayList<>();

        for (int i = 0; i < limit; i++) {
            result.add(planScores.get(i).getPlan());
        }

        return result;
    }

    @Getter
    @AllArgsConstructor
    private static class PlanScore {
        private final PersonalPlanRecommendResponse plan;
        private final double score;
    }

    // 요금제 조회
    @Override
    public List<PersonalPlanRecommendResponse> planList(PlanKeywordResponse keywordResponse) {
        return chatbotRepository.recommendPlans(keywordResponse);
    }

    private PlanKeywordResponse getKeyWordResponse(String question, Long userId) {
        PlanKeywordFirst resp1 = getLLMResponse(question, userId, prompt1, PlanKeywordFirst.class);
        PlanKeywordSecond resp2 = getLLMResponse(question, userId, prompt2, PlanKeywordSecond.class);
        PlanKeywordThird resp3 = getLLMResponse(question, userId, prompt3, PlanKeywordThird.class);

        log.info("planKeyword1 : {}", resp1);
        log.info("planKeyword2 : {}", resp2);
        log.info("planKeyword3 : {}", resp3);

        return  new PlanKeywordResponse(
                resp1.getCategory(), resp1.getPlanTarget(), resp1.getNetworkType(),
                resp2.getPrice(), resp2.getSpeedAfterLimit(), resp2.getCallAllowance(), resp2.getSmsAllowance(), resp2.getDataAllowance(), resp2.getTetheringSharing(),
                resp3.getMediaService(), resp3.getPremiumService()
        );
    }

    private <T> T getLLMResponse(String input, Long userId, String prompt, Class<T> responseType) {
        return chatClient.prompt()
                .system(prompt)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, userId))
                .user(input)
                .call()
                .entity(responseType);
    }
}
