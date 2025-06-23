package com.ureca.yoajungserver.chatbot.repository;

import static com.ureca.yoajungserver.plan.entity.QBenefit.benefit;
import static com.ureca.yoajungserver.plan.entity.QPlan.plan;
import static com.ureca.yoajungserver.plan.entity.QPlanBenefit.planBenefit;
import static org.springframework.util.StringUtils.hasText;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ureca.yoajungserver.chatbot.dto.BenefitEntry;
import com.ureca.yoajungserver.chatbot.dto.PersonalPlanListResponse;
import com.ureca.yoajungserver.chatbot.dto.PersonalPlanRecommendResponse;
import com.ureca.yoajungserver.chatbot.dto.PlanKeywordResponse;
import com.ureca.yoajungserver.plan.entity.BenefitType;
import com.ureca.yoajungserver.plan.entity.NetworkType;
import com.ureca.yoajungserver.plan.entity.PlanCategory;
import com.ureca.yoajungserver.plan.entity.PlanTarget;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;


@Slf4j
@Repository
@RequiredArgsConstructor
public class ChatbotRepositoryImpl implements ChatbotRepository {

    private final JPAQueryFactory jpaQueryFactory;

    // 챗봇 키워드 기반 맞춤 요금제 조회
    @Override
    public List<PersonalPlanRecommendResponse> recommendPlans(PlanKeywordResponse keyword) {

        BooleanBuilder havingBuilder = new BooleanBuilder();

        if (callCondition(keyword.getCallAllowance()) != null) {
            havingBuilder.and(new CaseBuilder()
                    .when(callCondition(keyword.getCallAllowance()))
                    .then(1).otherwise(0)
                    .max().eq(1));
        }
        if (smsCondition(keyword.getSmsAllowance()) != null) {
            havingBuilder.and(new CaseBuilder()
                    .when(smsCondition(keyword.getSmsAllowance()))
                    .then(1).otherwise(0)
                    .max().eq(1));
        }
        if (mediaCondition(keyword.getMediaService()) != null) {
            havingBuilder.and(new CaseBuilder()
                    .when(mediaCondition(keyword.getMediaService()))
                    .then(1).otherwise(0)
                    .max().eq(1));
        }
        if (premiumCondition(keyword.getPremiumService()) != null) {
            havingBuilder.and(new CaseBuilder()
                    .when(premiumCondition(keyword.getPremiumService()))
                    .then(1).otherwise(0)
                    .max().eq(1));
        }

        // 추천 요금제 리스트
        List<PersonalPlanListResponse> planList = jpaQueryFactory
                .select(Projections.constructor(PersonalPlanListResponse.class,
                        plan.id,
                        plan.name,
                        plan.planCategory,
                        plan.basePrice,
                        plan.networkType,
                        plan.dataAllowance,
                        plan.speedAfterLimit,
                        plan.tetheringSharingAllowance))
                .from(plan)
                .leftJoin(planBenefit).on(planBenefit.plan.id.eq(plan.id))
                .leftJoin(benefit).on(planBenefit.benefit.id.eq(benefit.id))
                .where(
                        planTargetCondition(keyword.getPlanTarget()),
                        categoryCondition(keyword.getCategory()),
                        priceCondition(keyword.getPrice()),
                        networkCondition(keyword.getNetworkType()),
                        dataCondition(keyword.getDataAllowance()),
                        afterLimitCondition(keyword.getSpeedAfterLimit()),
                        tetheringCondition(keyword.getTetheringSharing()))
                .groupBy(plan.id)
                .having(havingBuilder)
                .orderBy(plan.basePrice.desc())
                .fetch();

        // 추천 요금제와 혜택 매핑
        // 추천 요금제 ID 목록
        List<Long> planIdList = planList.stream()
                .map(PersonalPlanListResponse::getId)
                .collect(Collectors.toList());

        // 혜택 조회 (통화, 문자만)
        List<Tuple> benefits = jpaQueryFactory
                .select(planBenefit.plan.id, benefit.benefitType, benefit.voiceLimit, benefit.smsLimit)
                .from(planBenefit)
                .join(planBenefit.benefit, benefit)
                .where(planBenefit.plan.id.in(planIdList))
                .fetch();

        // 요금제 ID와 [혜택타입, 통화, 문자] 매핑
        Map<Long, List<BenefitEntry>> benefitMap = benefits.stream()
                .collect(Collectors.groupingBy(
                        t -> t.get(planBenefit.plan.id),
                        Collectors.mapping(t -> new BenefitEntry(
                                t.get(benefit.benefitType),
                                t.get(benefit.voiceLimit),
                                t.get(benefit.smsLimit)
                        ), Collectors.toList())
                ));

        // 최종 dto 리스트 변환
        List<PersonalPlanRecommendResponse> recommendResult = planList.stream()
                .map(planDto -> {
                    // 요금제 id 로 혜택 List 가져오기
                    List<BenefitEntry> benefitList = benefitMap.get(planDto.getId());

                    Integer call = null;
                    Integer sms = null;

                    if (benefitList != null) {
                        for (BenefitEntry entry : benefitList) {
                            if (entry.getBenefitType() == BenefitType.VOICE) {
                                call = entry.getVoiceLimit();
                            } else if (entry.getBenefitType() == BenefitType.SMS) {
                                sms = entry.getSmsLimit();
                            }
                        }
                    }

                    return new PersonalPlanRecommendResponse(planDto, call, sms);
                }).collect(Collectors.toList());

        return recommendResult;
    }

    // 분류 조건(ALL, YOUTH, SOLDIER, ...)
    private BooleanExpression planTargetCondition(String target) {
        if (isNull(target)) {
            return null;
        }
        return plan.planTarget.eq(PlanTarget.valueOf(target));
    }

    // 카테고리 조건
    private BooleanExpression categoryCondition(String category) {
        if (isNull(category)) {
            return null;
        }
        if (category.equals("일반")) {
            return plan.planCategory.eq(PlanCategory.LTE_FIVE_G);
        } else {
            return plan.planCategory.eq(PlanCategory.ONLINE_ONLY);
        }
    }

    // 가격 조건
    private BooleanExpression priceCondition(String price) {
        if (isNull(price)) {
            return null;
        }

        // 사잇값 처리
        if (price.contains("사이")) {
            Optional<int[]> opt = safeParseRange(price);
            if (opt.isPresent()) {
                int min = opt.get()[0];
                int max = opt.get()[1];
                return plan.basePrice.between(min, max);
            }
        }

        Optional<Integer> opt = safeParse(price);
        int priceInt;
        if (opt.isPresent()) {
            priceInt = opt.get();
        } else {
            return null;
        }

        if (price.contains("이상")) {
            return plan.basePrice.goe(priceInt);
        } else if (price.contains("정확")) {
            return plan.basePrice.eq(priceInt);
        } else if (price.contains("이하")) {
            return plan.basePrice.loe(priceInt);
        } else if (price.contains("초과")) {
            return plan.basePrice.gt(priceInt);
        } else if (price.contains("미만")) {
            return plan.basePrice.lt(priceInt);
        } else if (price.contains("평균")) {
            int min = (priceInt / 10000) * 10000;
            int max = min + 10000;
            return plan.basePrice.between(min, max - 1);
        }

        return plan.basePrice.eq(priceInt);
    }

    // 네트워크 타입 조건
    private BooleanExpression networkCondition(String networkType) {
        if (isNull(networkType)) {
            return null;
        }
        return plan.networkType.eq(NetworkType.valueOf(networkType));
    }

    // 통화량 조건
    private BooleanExpression callCondition(String call) {
        if (isNull(call)) {
            return null;
        }

        if (call.equals("무제한")) {
            return benefit.voiceLimit.eq(9999);
        } else {
            Optional<Integer> opt = safeParse(call);
            int callInt;

            if (opt.isPresent()) {
                callInt = opt.get();
            } else {
                return null;
            }

            return benefit.voiceLimit.goe(callInt);
        }
    }

    // 문자량 조건
    private BooleanExpression smsCondition(String sms) {
        if (isNull(sms)) {
            return null;
        }

        if (sms.equals("무제한")) {
            return benefit.smsLimit.eq(9999);
        } else {
            Optional<Integer> opt = safeParse(sms);
            int smsInt;

            if (opt.isPresent()) {
                smsInt = opt.get();
            } else {
                return null;
            }
            return benefit.smsLimit.goe(smsInt);
        }
    }

    // 데이터 사용량 조건
    private BooleanExpression dataCondition(String data) {
        if (isNull(data)) {
            return null;
        }

        switch (data) {
            case "무제한":
                return plan.dataAllowance.eq(9999).or(plan.speedAfterLimit.goe(3));
            case "보통":
                return plan.dataAllowance.between(30, 110);
            case "적음":
                return plan.dataAllowance.lt(30);
            case "":
                return null;
        }

        // 사잇값 처리
        if (data.contains("사이")) {
            Optional<int[]> opt = safeParseRange(data);
            if (opt.isPresent()) {
                int min = opt.get()[0];
                int max = opt.get()[1];
                return plan.dataAllowance.between(min, max);
            }
        }

        Optional<Integer> opt = safeParse(data);
        int dataInt;

        if (opt.isPresent()) {
            dataInt = opt.get();
        } else {
            return null;
        }

        if (data.contains("이상")) {
            return plan.dataAllowance.goe(dataInt);
        } else if (data.contains("평균")) {
            int min = (dataInt / 10) * 10 - 20;
            int max = min + 40;
            System.out.println("min : " + min);
            System.out.println("max : " + max);
            return plan.dataAllowance.between(min, max - 1);
        } else if (data.contains("이하")) {
            return plan.dataAllowance.loe(dataInt);
        } else if (data.contains("초과")) {
            return plan.dataAllowance.gt(dataInt);
        } else if (data.contains("미만")) {
            return plan.dataAllowance.lt(dataInt);
        } else if (data.contains("정확")) {
            return plan.dataAllowance.eq(dataInt);
        }

        return plan.dataAllowance.eq(dataInt);
    }

    // 속도제한 조건
    private BooleanExpression afterLimitCondition(String afterLimit) {
        if (isNull(afterLimit)) {
            return null;
        }

        // 사잇값 처리
        if (afterLimit.contains("사이")) {
            Optional<int[]> opt = safeParseRange(afterLimit);
            if (opt.isPresent()) {
                int min = opt.get()[0];
                int max = opt.get()[1];
                return plan.speedAfterLimit.between(min, max);
            }
        }

        Optional<Integer> opt = safeParse(afterLimit);
        int afterLimitInt;

        if (opt.isPresent()) {
            afterLimitInt = opt.get();
        } else {
            return null;
        }

        if (afterLimit.contains("이상") || afterLimit.contains("평균")) {
            return plan.speedAfterLimit.goe(afterLimitInt);
        } else if (afterLimit.contains("정확")) {
            return plan.speedAfterLimit.eq(afterLimitInt);
        } else if (afterLimit.contains("이하")) {
            return plan.speedAfterLimit.loe(afterLimitInt);
        } else if (afterLimit.contains("초과")) {
            return plan.speedAfterLimit.gt(afterLimitInt);
        } else if (afterLimit.contains("미만")) {
            return plan.speedAfterLimit.lt(afterLimitInt);
        }

        if (afterLimitInt > 0) {
            return plan.speedAfterLimit.goe(afterLimitInt);
        }

        return plan.speedAfterLimit.eq(afterLimitInt);
    }

    // 테더링 조건
    private BooleanExpression tetheringCondition(String tethering) {
        if (isNull(tethering)) {
            return null;
        }

        // 사잇값 처리
        if (tethering.contains("사이")) {
            Optional<int[]> opt = safeParseRange(tethering);
            if (opt.isPresent()) {
                int min = opt.get()[0];
                int max = opt.get()[1];
                return plan.tetheringSharingAllowance.between(min, max);
            }
        }

        Optional<Integer> opt = safeParse(tethering);
        int tetheringInt;

        if (opt.isPresent()) {
            tetheringInt = opt.get();
        } else {
            return null;
        }

        if (tethering.contains("이상") || tethering.contains("평균")) {
            return plan.tetheringSharingAllowance.goe(tetheringInt);
        } else if (tethering.contains("정확")) {
            return plan.tetheringSharingAllowance.eq(tetheringInt);
        } else if (tethering.contains("이하")) {
            return plan.tetheringSharingAllowance.loe(tetheringInt);
        } else if (tethering.contains("초과")) {
            return plan.tetheringSharingAllowance.gt(tetheringInt);
        } else if (tethering.contains("미만")) {
            return plan.tetheringSharingAllowance.lt(tetheringInt);
        }

        return plan.tetheringSharingAllowance.eq(tetheringInt);
    }

    private boolean isNull(String input) {
        return !hasText(input) || input.equals("null");
    }

    // 미디어 서비스 조건
    private BooleanExpression mediaCondition(String media) {
        if (hasText(media) && !media.equals("null")) {
            return benefit.benefitType.eq(BenefitType.MEDIA);
        }
        return null;
    }

    // 프리미엄 서비스 조건
    private BooleanExpression premiumCondition(String premium) {
        if (hasText(premium) && !premium.equals("null")) {
            return benefit.benefitType.eq(BenefitType.PREMIUM);
        }
        return null;
    }

    private Optional<Integer> safeParse(String input) {
        try {
            return Optional.of(Integer.parseInt(input.replaceAll("[^0-9]", "")));
        } catch (NumberFormatException e) {
            log.warn("잘못된 숫자 포맷: {}", input);
            return Optional.empty();
        }
    }

    private Optional<int[]> safeParseRange(String input) {
        String[] parse = input.split(",");

        if (parse[0].contains("무제한")) {
            parse[0] = "9999";
        }
        if (parse[1].contains("무제한")) {
            parse[1] = "9999";
        }

        if (parse.length != 2) {
            return Optional.empty();
        }

        Optional<Integer> min = safeParse(parse[0]);
        Optional<Integer> max = safeParse(parse[1]);

        if (min.isPresent() && max.isPresent()) {
            return Optional.of(new int[]{min.get(), max.get()});
        }

        return Optional.empty();
    }

}