package com.ureca.yoajungserver.plan.service;

import com.ureca.yoajungserver.plan.dto.response.DetailBenefitDto;
import com.ureca.yoajungserver.plan.dto.response.PlanRecommendResponse;
import com.ureca.yoajungserver.plan.entity.Benefit;
import com.ureca.yoajungserver.plan.entity.Plan;
import com.ureca.yoajungserver.plan.entity.PlanBenefit;
import com.ureca.yoajungserver.plan.entity.PlanProduct;
import com.ureca.yoajungserver.plan.repository.PlanRepository;
import com.ureca.yoajungserver.plan.exception.PlanNotFoundException;
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
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanRecommendServiceImpl implements PlanRecommendService {

    private final ChatClient.Builder chatClientBuilder;
    private final UserRepository userRepository;
    private final TendencyRepository tendencyRepository;
    private final PlanRepository planRepository;

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
    @Transactional
    public PlanRecommendResponse getRecommendPlan(Long planId, CustomUserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getEmail()).orElseThrow(UserNotFoundException::new);
        Tendency tendency = tendencyRepository.findByUser(user).orElseThrow(TendencyNotFoundException::new);
        Plan plan = planRepository.findById(planId).orElseThrow(PlanNotFoundException::new);

        // 혜택 문자열로 변환
        String benefitsStr = plan.getPlanBenefits().stream()
                .map(benefit -> String.format("%s(%s)",
                        benefit.getBenefit().getName(),
                        benefit.getBenefit().getDescription()))
                .collect(Collectors.joining(", "));

        // 상품 문자열로 변환
        String productsStr = plan.getPlanProducts().stream()
                .map(product -> String.format("%s",
                        product.getProduct().getName()))
                .collect(Collectors.joining(", "));



        String input = String.format(
                "[사용자 성향] 월평균 데이터 %sGB, 월평균 통화 %s분, 희망 요금제 가격 %s원, 기타: %s\n" +
                        "[요금제 정보] 이름=%s, 카테고리=%s, 네트워크=%s, 기본요금 %d원, 데이터 %sGB, " +
                        "테더링 %dGB, 속도제한 후 +%sMbps, 설명= %s, 대상= %s, 혜택= %s, 상품= %s\n",

                tendency.getAvgMonthlyDataGB(),
                tendency.getAvgMonthlyVoiceMin(),
                tendency.getPreferencePrice(),
                tendency.getComment(),
                // DetailPlanRequest 필드
                plan.getName(),
                plan.getPlanCategory(),
                plan.getNetworkType(),
                plan.getBasePrice(),
                plan.getDataAllowance(),
                plan.getTetheringSharingAllowance(),
                plan.getSpeedAfterLimit(),
                plan.getDescription(),
                plan.getPlanTarget(),
                // 추가 혜택
                benefitsStr,
                productsStr
        );

        return chatClient.prompt()
                .system(prompt)
                .user(input)
                .call()
                .entity(PlanRecommendResponse.class);
    }
}
