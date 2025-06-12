package com.ureca.yoajungserver.chatbot.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ureca.yoajungserver.chatbot.dto.BenefitEntry;
import com.ureca.yoajungserver.chatbot.dto.PersonalPlanListResponse;
import com.ureca.yoajungserver.chatbot.dto.PlanKeywordResponse;
import com.ureca.yoajungserver.chatbot.dto.PersonalPlanRecommendResponse;
import com.ureca.yoajungserver.plan.entity.BenefitType;
import com.ureca.yoajungserver.plan.entity.NetworkType;
import com.ureca.yoajungserver.plan.entity.PlanCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ureca.yoajungserver.plan.entity.QPlan.plan;
import static com.ureca.yoajungserver.plan.entity.QPlanBenefit.planBenefit;
import static com.ureca.yoajungserver.plan.entity.QBenefit.benefit;
import static org.springframework.util.StringUtils.hasText;


@Slf4j
@Repository
@RequiredArgsConstructor
public class ChatbotRepositoryImpl implements ChatbotRepository {

    private final JPAQueryFactory jpaQueryFactory;

    // 챗봇 키워드 기반 맞춤 요금제 조회
    @Override
    public List<PersonalPlanRecommendResponse> recommendPlans(PlanKeywordResponse keyword) {

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
                        categoryCondition(keyword.getCategory()),
                        priceCondition(keyword.getPrice()),
                        networkCondition(keyword.getNetworkType()),
                        dataCondition(keyword.getDataAllowance()),
                        tetheringCondition(keyword.getTetheringSharing()))
                .groupBy(plan.id)
                .having(
                        // Benefit 조건
                        new CaseBuilder()
                                .when(callCondition(keyword.getCallAllowance()))
                                .then(1).otherwise(0)
                                .max().eq(1),
                        new CaseBuilder()
                                .when(smsCondition(keyword.getSmsAllowance()))
                                .then(1).otherwise(0)
                                .max().eq(1),
                        new CaseBuilder()
                                .when(mediaCondition(keyword.getMediaService()))
                                .then(1).otherwise(0)
                                .max().eq(1),
                        new CaseBuilder()
                                .when(premiumCondition(keyword.getPremiumService()))
                                .then(1).otherwise(0)
                                .max().eq(1)
                )
                .orderBy(plan.basePrice.desc())
//                .limit(3)
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
                            if(entry.getBenefitType() == BenefitType.VOICE)
                                call = entry.getVoiceLimit();
                            else if(entry.getBenefitType() == BenefitType.SMS)
                                sms = entry.getSmsLimit();
                        }
                    }

                    return new PersonalPlanRecommendResponse(planDto, call, sms);
                }).collect(Collectors.toList());

        return recommendResult;
    }

    // 카테고리 조건
    private BooleanExpression categoryCondition(String category) {
        if(!hasText(category))
            return null;
        if(category.equals("일반"))
            return plan.planCategory.eq(PlanCategory.LTE_FIVE_G);
        else
            return plan.planCategory.eq(PlanCategory.ONLINE_ONLY);
    }

    // 가격 조건
    private BooleanExpression priceCondition(Integer price) {
        if(price != null)
            return plan.basePrice.loe(price);
        return null;
    }

    // 네트워크 타입 조건
    private BooleanExpression networkCondition(String networkType) {
        return plan.networkType.eq(NetworkType.valueOf(networkType));
    }

    // 통화량 조건
    private BooleanExpression callCondition(String call) {
        if(call.equals("무제한"))
            return benefit.voiceLimit.eq(9999);
        else
            return benefit.voiceLimit.goe(Integer.parseInt(call));
    }

    // 문자량 조건
    private BooleanExpression smsCondition(String sms) {
        if(sms.equals("무제한"))
            return benefit.smsLimit.eq(9999);
        else
            return benefit.smsLimit.goe(Integer.parseInt(sms));
    }

    // 데이터 사용량 조건
    private BooleanExpression dataCondition(String data) {
        switch(data){
            case "무제한" : return plan.dataAllowance.eq(9999).or(plan.speedAfterLimit.goe(3));
            case "보통" : return plan.dataAllowance.between(30, 110);
            case "적음" : return plan.dataAllowance.lt(30);
            case "" : return null;
            default : return plan.dataAllowance.goe(Integer.parseInt(data));
        }
    }

    // 테더링 조건
    private BooleanExpression tetheringCondition(String tethering) {
        if(hasText(tethering))
            return plan.tetheringSharingAllowance.goe(Integer.parseInt(tethering));
        return null;
    }

    // 미디어 서비스 조건
    private BooleanExpression mediaCondition(String media) {
        if(media.equals("Y"))
            return benefit.benefitType.eq(BenefitType.MEDIA);
        return JPAExpressions
                .selectOne()
                .from(planBenefit)
                .join(planBenefit.benefit, benefit)
                .where(
                        planBenefit.plan.id.eq(plan.id),
                        benefit.benefitType.eq(BenefitType.MEDIA)
                ).notExists();
    }

    // 프리미엄 서비스 조건
    private BooleanExpression premiumCondition(String premium) {
        if(premium.equals("Y"))
            return benefit.benefitType.eq(BenefitType.PREMIUM);
        return JPAExpressions
                .selectOne()
                .from(planBenefit)
                .join(planBenefit.benefit, benefit)
                .where(
                        planBenefit.plan.id.eq(plan.id),
                        benefit.benefitType.eq(BenefitType.PREMIUM)
                ).notExists();
    }

}