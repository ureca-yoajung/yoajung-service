package com.ureca.yoajungserver.plan.service;

import com.ureca.yoajungserver.plan.dto.request.PlanFilterRequest;
import com.ureca.yoajungserver.plan.dto.response.PlanFilterResponse;
import com.ureca.yoajungserver.plan.dto.response.PlanFilterResultResponse;
import com.ureca.yoajungserver.plan.entity.Plan;
import com.ureca.yoajungserver.plan.repository.PlanFilterRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanFilterServiceImpl implements PlanFilterService {

    private final PlanFilterRepository planFilterRepository;

    @Override
    @Transactional
    public PlanFilterResultResponse searchPlans(PlanFilterRequest request) {
        // Fetch paginated plans and total count using consistent filter logic
        List<Plan> plans = planFilterRepository.plans(request);
        long totalCount = planFilterRepository.countPlans(request);
        return PlanFilterResultResponse.builder()
                .plans(plans.stream().map(PlanFilterResponse::from).toList())
                .totalCount(totalCount)
                .build();
    }


}
