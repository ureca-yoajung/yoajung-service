package com.ureca.yoajungserver.summary.repository;

import com.ureca.yoajungserver.summary.entity.PlanSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanSummaryRepository extends JpaRepository<PlanSummary, Long> {
}