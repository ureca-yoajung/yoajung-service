package com.ureca.yoajungserver.plan.service;

import com.ureca.yoajungserver.plan.dto.response.*;
import com.ureca.yoajungserver.plan.entity.*;
import com.ureca.yoajungserver.plan.repository.PlanBenefitRepository;
import com.ureca.yoajungserver.plan.repository.PlanProductRepository;
import com.ureca.yoajungserver.plan.repository.PlanRepository;
import com.ureca.yoajungserver.plan.exception.PlanNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ureca.yoajungserver.common.BaseCode.PLAN_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final PlanProductRepository planProductRepository;
    private final PlanBenefitRepository planBenefitRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ListPlanResponse> getListPlan(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Plan> planPage = planRepository.findAll(pageable);

        return planPage.getContent().stream()
                .map(plan -> {
                    List<ListBenefitDto> benefitDtos = plan.getPlanBenefits().stream()
                            .map(PlanBenefit::getBenefit)
                            .distinct() // Benefit 객체 기준으로 중복 제거
                            .map(ListBenefitDto::fromBenefit)
                            .collect(Collectors.toList());

                    List<ListProductDto> serviceDtos = plan.getPlanProducts().stream()
                            .map(PlanProduct::getProduct)
                            .map(ListProductDto::fromService)
                            .collect(Collectors.toList());

                    return ListPlanResponse.fromPlan(plan, serviceDtos, benefitDtos);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<DetailProductDto> getListPlanProducts(Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(PLAN_NOT_FOUND));

        List<PlanProduct> planProducts = planProductRepository.findByPlanId(plan.getId());

        return planProducts.stream()
                .map(product -> DetailProductDto.fromProduct(product.getProduct()))
                .collect(Collectors.toList());
    }

    @Override
    public DetailPlanBenefitResponse getDetailPlanBenefit(Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(PLAN_NOT_FOUND));

        List<PlanBenefit> planBenefits = planBenefitRepository.findByPlanId(plan.getId());

        Map<BenefitType, Benefit> benefitMap = planBenefits.stream()
                .map(PlanBenefit::getBenefit)
                .collect(Collectors.toMap(Benefit::getBenefitType, Function.identity(), (a, b) -> a));

        return DetailPlanBenefitResponse.builder()
                .voice(DetailBenefitDto.fromBenefit(benefitMap.get(BenefitType.VOICE)))
                .sms(DetailBenefitDto.fromBenefit(benefitMap.get(BenefitType.SMS)))
                .media(DetailBenefitDto.fromBenefit(benefitMap.get(BenefitType.MEDIA)))
                .smartDevice(DetailBenefitDto.fromBenefit(benefitMap.get(BenefitType.SMART_DEVICE)))
                .base(DetailBenefitDto.fromBenefit(benefitMap.get(BenefitType.BASE)))
                .premium(DetailBenefitDto.fromBenefit(benefitMap.get(BenefitType.PREMIUM)))
                .other(DetailBenefitDto.fromBenefit(benefitMap.get(BenefitType.OTHER)))
                .build();
    }
}
