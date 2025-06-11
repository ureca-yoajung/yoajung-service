package com.ureca.yoajungserver.plan.service;

import com.ureca.yoajungserver.plan.dto.request.PlanFilterRequest;
import com.ureca.yoajungserver.plan.dto.response.PlanFilterResponse;
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
    public List<PlanFilterResponse> searchPlans(PlanFilterRequest filterRequest) {
        return planFilterRepository.plans(filterRequest)
                .stream().map(PlanFilterResponse::from)
                .collect(Collectors.toList());
    }
}
