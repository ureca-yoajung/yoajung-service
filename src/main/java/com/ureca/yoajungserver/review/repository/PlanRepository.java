package com.ureca.yoajungserver.review.repository;

import com.ureca.yoajungserver.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 임시 PlanRepository. 머지하고 지우기
 */
public interface PlanRepository extends JpaRepository<Plan, Long> {

}
