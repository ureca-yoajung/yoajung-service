package com.ureca.yoajungserver.plan.repository;

import com.ureca.yoajungserver.plan.entity.PlanBenefit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanBenefitRepository extends JpaRepository<PlanBenefit, Long> {
    List<PlanBenefit> findByPlanId(Long planId);
}
