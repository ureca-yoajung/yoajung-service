package com.ureca.yoajungserver.summary.service;

import com.ureca.yoajungserver.summary.dto.PlanSummaryDto;
import com.ureca.yoajungserver.summary.repository.PlanSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanSummaryService {
    private final PlanSummaryRepository planSummaryRepository;

//    @Cacheable(value = "planSummary", key = "#planId", unless = "#result == null")
    public PlanSummaryDto getSummary(Long planId) {
        return planSummaryRepository.findById(planId)
                .map(PlanSummaryDto::fromEntity)
                .orElse(null);
    }
}