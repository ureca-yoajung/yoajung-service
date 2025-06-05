package com.ureca.yoajungserver.plan.service;

import com.ureca.yoajungserver.plan.dto.response.ListBenefitDto;
import com.ureca.yoajungserver.plan.dto.response.ListPlanResponse;
import com.ureca.yoajungserver.plan.dto.response.ListServiceDto;
import com.ureca.yoajungserver.plan.entity.Plan;
import com.ureca.yoajungserver.plan.entity.PlanBenefit;
import com.ureca.yoajungserver.plan.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;

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

                    List<ListServiceDto> serviceDtos = plan.getPlanServices().stream()
                            .map(com.ureca.yoajungserver.plan.entity.PlanService::getService)
                            .map(ListServiceDto::fromService)
                            .collect(Collectors.toList());

                    return ListPlanResponse.fromPlan(plan, serviceDtos, benefitDtos);
                })
                .collect(Collectors.toList());
    }
}
