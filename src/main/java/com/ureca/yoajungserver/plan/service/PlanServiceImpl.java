package com.ureca.yoajungserver.plan.service;

import com.ureca.yoajungserver.plan.dto.response.DetailProductDto;
import com.ureca.yoajungserver.plan.dto.response.ListBenefitDto;
import com.ureca.yoajungserver.plan.dto.response.ListPlanResponse;
import com.ureca.yoajungserver.plan.dto.response.ListProductDto;
import com.ureca.yoajungserver.plan.entity.Plan;
import com.ureca.yoajungserver.plan.entity.PlanBenefit;
import com.ureca.yoajungserver.plan.entity.PlanProduct;
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
import java.util.stream.Collectors;

import static com.ureca.yoajungserver.common.BaseCode.PLAN_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final PlanProductRepository planProductRepository;

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
}
