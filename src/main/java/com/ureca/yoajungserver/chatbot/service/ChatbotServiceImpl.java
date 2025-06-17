package com.ureca.yoajungserver.chatbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.ureca.yoajungserver.chatbot.dto.PersonalPlanRecommendResponse;
import com.ureca.yoajungserver.chatbot.dto.PlanKeywordFirst;
import com.ureca.yoajungserver.chatbot.dto.PlanKeywordResponse;
import com.ureca.yoajungserver.chatbot.dto.PlanKeywordSecond;
import com.ureca.yoajungserver.chatbot.dto.PlanKeywordThird;


import com.ureca.yoajungserver.chatbot.repository.ChatbotRepository;
import com.ureca.yoajungserver.user.entity.Tendency;
import com.ureca.yoajungserver.user.entity.User;
import com.ureca.yoajungserver.user.exception.TendencyNotFoundException;
import com.ureca.yoajungserver.user.exception.UserNotFoundException;
import com.ureca.yoajungserver.user.repository.TendencyRepository;
import com.ureca.yoajungserver.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;



import java.util.concurrent.CompletableFuture;
import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.chat.memory.ChatMemory;

import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;

import org.springframework.ai.chat.messages.SystemMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Comparator;


@Slf4j
@Service
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService {
    private final ObjectMapper objectMapper;
    private final ChatbotRepository chatbotRepository;
    private final LLMAsyncService llmAsyncService;
    private final UserRepository userRepository;
    private final TendencyRepository tendencyRepository;


    private final ChatMemory chatMemory;
    private final JdbcChatMemoryRepository chatMemoryRepository;

    // application.yml 의 키와 일치시킵니다.

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
        prompt1 = readPrompt(promptRes1);
        prompt2 = readPrompt(promptRes2);
        prompt3 = readPrompt(promptRes3);
    }

    private String readPrompt(Resource resource) throws IOException {
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    @Override
    public List<PersonalPlanRecommendResponse> keywordMapper(String input, String userId) {
        try {
            // 3. 각 Future에서 완료된 결과를 추출합니다.
            //    .join()은 예외를 던지지 않지만, .get()은 checked exception을 던집니다.
            //    allOf() 로 이미 완료를 기다렸기 때문에 여기서 join()은 블로킹되지 않습니다.
            PlanKeywordResponse planKeywordResponse = getKeyWordResponse(input, userId);

            // DB 조회
            List<PersonalPlanRecommendResponse> planResult = chatbotRepository.recommendPlans(planKeywordResponse); // json으로

            // 조회 결과 db에 저장
            String json = objectMapper.writeValueAsString(planResult);
            Message message = new SystemMessage(json);
            chatMemory.add(userId, message);

            return planResult;
        } catch (Exception e) {
            // 비동기 작업 중 발생한 예외 처리
            log.error("Error during asynchronous LLM call processing", e);
            // 필요에 따라 사용자 정의 예외를 던지거나 다른 방식으로 처리할 수 있습니다.
            throw new RuntimeException("Failed to get response from LLM.", e);
        }
    }

    @Override
    public List<PersonalPlanRecommendResponse> keywordMapperByPreferences(String question, Long userId) {
        PlanKeywordResponse planKeywordResponse = getKeyWordResponse(question, userId.toString());

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

    // 요금제 조회
    @Override
    public List<PersonalPlanRecommendResponse> planList(PlanKeywordResponse keywordResponse) {
        return chatbotRepository.recommendPlans(keywordResponse);
    }

    private PlanKeywordResponse getKeyWordResponse(String input, String userId){
        // 1. 각 LLM 호출을 비동기로 시작합니다.
        //    이 메소드들은 호출 즉시 CompletableFuture 객체를 반환하고, 백그라운드 스레드에서 실행됩니다.
        CompletableFuture<PlanKeywordFirst> future1 = llmAsyncService.getLLMResponse(input, userId, prompt1, PlanKeywordFirst.class);
        CompletableFuture<PlanKeywordSecond> future2 = llmAsyncService.getLLMResponse(input, userId, prompt2, PlanKeywordSecond.class);
        CompletableFuture<PlanKeywordThird> future3 = llmAsyncService.getLLMResponse(input, userId, prompt3, PlanKeywordThird.class);

        // 2. 모든 비동기 작업이 완료될 때까지 메인 스레드를 대기시킵니다.
        CompletableFuture.allOf(future1, future2, future3).join();

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

        return planKeywordResponse;
    }

    @Getter
    @AllArgsConstructor
    private static class PlanScore {
        private final PersonalPlanRecommendResponse plan;
        private final double score;
    }

}

