package com.ureca.yoajungserver.plan.service;

import com.ureca.yoajungserver.plan.dto.response.*;
import com.ureca.yoajungserver.plan.entity.*;
import com.ureca.yoajungserver.plan.repository.PlanBenefitRepository;
import com.ureca.yoajungserver.plan.repository.PlanProductRepository;
import com.ureca.yoajungserver.plan.repository.PlanRepository;
import com.ureca.yoajungserver.plan.exception.PlanNotFoundException;
import com.ureca.yoajungserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ureca.yoajungserver.common.BaseCode.PLAN_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final PlanProductRepository planProductRepository;
    private final PlanBenefitRepository planBenefitRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public ListPlanResponse getListPlan(int page, int size, PlanCategory planCategory, PlanSortType sortedType) {
        Pageable pageable = PlanSortType.toSort(sortedType)
                .map(sort -> PageRequest.of(page, size, sort))
                .orElse(PageRequest.of(page, size));

        Page<Plan> planPage = (planCategory == null)
                ? planRepository.findAll(pageable)
                : planRepository.findAllByPlanCategory(planCategory, pageable);

        return ListPlanResponse.builder()
                .plans(convertToListPlanResponse(planPage.getContent()))
                .count(planPage.getTotalElements())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ListPlanResponse getPopularPlans(int page, int size, PlanCategory planCategory, PlanSortType sortedType) {
        Pageable pageable;

        LocalDateTime today = LocalDate.now().atStartOfDay(); // 2025-06-09T00:00
        LocalDateTime endDate = today;                        // 오늘 0시
        LocalDateTime startDate = today.minusDays(7);         // 7일 전 0시

        List<Long> popularPlanIds = userRepository.findPopularPlansInDateRange(startDate, endDate);

        long totalCount;

        if (planCategory == null) {
            totalCount = planRepository.count();
        } else {
            totalCount = planRepository.countByPlanCategory(planCategory);
        }

        if (popularPlanIds.isEmpty()) {
            Sort sort = PlanSortType.toSort(sortedType != null ? sortedType : PlanSortType.HIGH_DATA).orElse(Sort.unsorted());
            pageable = PageRequest.of(page, size, sort);
            Page<Plan> fallbackPlans = (planCategory == null)
                    ? planRepository.findAll(pageable)
                    : planRepository.findAllByPlanCategory(planCategory, pageable);

            return ListPlanResponse.builder()
                    .plans(convertToListPlanResponse(fallbackPlans.getContent()))
                    .count(fallbackPlans.getTotalElements())
                    .build();
        }

        // 인기 요금제를 순서대로 조회
        List<Plan> plans = (planCategory == null)
                ? planRepository.findByIdIn(popularPlanIds)
                : planRepository.findByPlanCategoryAndIdIn(planCategory, popularPlanIds);

        // 인기순으로 정렬
        Map<Long, Plan> planMap = plans.stream()
                .collect(Collectors.toMap(Plan::getId, plan -> plan));

        List<Plan> popularPlans =  popularPlanIds.stream()
                .map(planMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        int requestedStart = page * size;
        int requestedEnd = requestedStart + size;

        List<Plan> result = new ArrayList<>();

        // 인기 요금제에서 현재 페이지에 해당하는 부분 추가
        if (requestedStart < popularPlans.size()) {
            int popularEnd = Math.min(requestedEnd, popularPlans.size());
            result.addAll(popularPlans.subList(requestedStart, popularEnd));
        }

        // 일반 요금제로 부족한 부분 채우기
        int remainingCount = size - result.size();
        if (remainingCount > 0) {
            // 일반 요금제에서의 실제 오프셋 계산
            int normalOffset = Math.max(0, requestedStart - plans.size());

            // 조회할 개수 계산 (안전장치 포함)
            int normalFetchSize = remainingCount + (normalOffset % size);
            int safeFetchSize = Math.min(normalFetchSize, 30); // 상한 30개로 제한

            Pageable normalPageable = PageRequest.of(
                    normalOffset / size,  // 일반 요금제에서의 페이지 번호
                    safeFetchSize,
                    Sort.by(Sort.Direction.DESC, "dataAllowance"));

            Page<Plan> normalPlans = (planCategory == null)
                    ? planRepository.findByIdNotIn(popularPlanIds, normalPageable)
                    : planRepository.findByPlanCategoryAndIdNotIn(planCategory, popularPlanIds, normalPageable);

            // 정확한 시작 위치부터 필요한 개수만큼 추가
            List<Plan> normalContent = normalPlans.getContent();
            int skipCount = normalOffset % size;
            int endIndex = Math.min(skipCount + remainingCount, normalContent.size());

            if (skipCount < normalContent.size()) {
                result.addAll(normalContent.subList(skipCount, endIndex));
            }
        }

        return ListPlanResponse.builder()
                .plans(convertToListPlanResponse(result))
                .count(totalCount)
                .build();
    }

    @Override
    public DetailPlanResponse getDetailPlan(Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(PLAN_NOT_FOUND));

        return DetailPlanResponse.fromPlan(plan);
    }

    @Override
    public DetailPlanProductResponse getDetailPlanProducts(Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(PLAN_NOT_FOUND));

        List<PlanProduct> planProducts = planProductRepository.findByPlanIdWithProduct(planId);

        List<Product> products = planProducts.stream()
                .map(PlanProduct::getProduct)
                .toList();

        List<DetailProductDto> media = products.stream()
                .filter(p -> p.getProductCategory() == ProductCategory.MEDIA)
                .map(DetailProductDto::fromProduct)
                .toList();

        List<DetailProductDto> premium = products.stream()
                .filter(p -> p.getProductCategory() == ProductCategory.PREMIUM)
                .map(DetailProductDto::fromProduct)
                .toList();

        return DetailPlanProductResponse.builder()
                .media(media)
                .premium(premium)
                .build();
    }

    @Override
    public DetailPlanBenefitResponse getDetailPlanBenefit(Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(PLAN_NOT_FOUND));

        List<PlanBenefit> planBenefits = planBenefitRepository.findByPlanIdWithBenefit(planId);

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

    private List<ListPlanDto> convertToListPlanResponse(List<Plan> plans) {
        return plans.stream()
                .map(plan -> {
                    List<ListBenefitDto> benefitDtos = plan.getPlanBenefits().stream()
                            .map(PlanBenefit::getBenefit)
                            .distinct()
                            .map(ListBenefitDto::fromBenefit)
                            .collect(Collectors.toList());

                    List<ListProductDto> serviceDtos = plan.getPlanProducts().stream()
                            .map(PlanProduct::getProduct)
                            .map(ListProductDto::fromService)
                            .collect(Collectors.toList());

                    return ListPlanDto.fromPlan(plan, serviceDtos, benefitDtos);
                })
                .collect(Collectors.toList());
    }
}
