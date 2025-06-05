package com.ureca.yoajungserver.plan.repository;

import com.ureca.yoajungserver.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {
}
