package com.ureca.yoajungserver.plan.service;

import com.ureca.yoajungserver.plan.dto.request.DetailPlanRequest;
import com.ureca.yoajungserver.plan.dto.response.PlanRecommendResponse;
import com.ureca.yoajungserver.user.entity.Tendency;
import com.ureca.yoajungserver.user.entity.User;
import com.ureca.yoajungserver.user.exception.TendencyNotFoundException;
import com.ureca.yoajungserver.user.exception.UserNotFoundException;
import com.ureca.yoajungserver.user.repository.TendencyRepository;
import com.ureca.yoajungserver.user.repository.UserRepository;
import com.ureca.yoajungserver.user.security.CustomUserDetails;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class PlanRecommendServiceImpl implements PlanRecommendService {

    private final ChatClient.Builder chatClientBuilder;
    private final UserRepository userRepository;
    private final TendencyRepository tendencyRepository;

    private ChatClient chatClient;

    @Value("${spring.ai.chat.system-prompt6}")
    private Resource promptRes6;
    private String prompt;


    @PostConstruct
    public void init() throws IOException {
        chatClient = chatClientBuilder.build();
        prompt = readPrompt(promptRes6);
    }

    private String readPrompt(Resource resource) throws IOException {
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    @Override
    public PlanRecommendResponse getRecommendPlan(DetailPlanRequest detailPlanRequest, CustomUserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getEmail()).orElseThrow(UserNotFoundException::new);
        Tendency tendency = tendencyRepository.findByUser(user).orElseThrow(TendencyNotFoundException::new);

        String input = String.format(
                "사용자 성향: 월평균 데이터 %sGB, 월평균 통화 %s분, 희망 요금제 가격 %s원, 기타: %s\n" +
                        "요금제: 이름=%s, 카테고리=%s, 네트워크=%s, 기본요금 %d원, 데이터 %sGB, " +
                        "테더링 %dGB, 속도제한 후 +%sMbps, 설명=%s, 대상=%s\n",

                tendency.getAvgMonthlyDataGB(),
                tendency.getAvgMonthlyVoiceMin(),
                tendency.getPreferencePrice(),
                tendency.getComment(),
                // DetailPlanRequest 필드
                detailPlanRequest.getName(),
                detailPlanRequest.getPlanCategory(),
                detailPlanRequest.getNetworkType(),
                detailPlanRequest.getBasePrice(),
                detailPlanRequest.getDataAllowance(),
                detailPlanRequest.getTetheringSharingAllowance(),
                detailPlanRequest.getSpeedAfterLimit(),
                detailPlanRequest.getDescription(),
                detailPlanRequest.getPlanTarget()
        );

        return chatClient.prompt()
                .system(prompt)
                .user(input)
                .call()
                .entity(PlanRecommendResponse.class);
    }
}
