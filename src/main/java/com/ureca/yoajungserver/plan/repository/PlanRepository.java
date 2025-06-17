package com.ureca.yoajungserver.plan.repository;

import com.ureca.yoajungserver.plan.entity.Plan;
import com.ureca.yoajungserver.plan.entity.PlanCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    Page<Plan> findAllByPlanCategory(PlanCategory planCategory, Pageable pageable);

    List<Plan> findByIdIn(List<Long> planIds);

    List<Plan> findByPlanCategoryAndIdIn(PlanCategory planCategory, List<Long> planIds);

    Page<Plan> findByIdNotIn(List<Long> popularPlanIds, Pageable normalPageable);

    Page<Plan> findByPlanCategoryAndIdNotIn(PlanCategory planCategory, List<Long> popularPlanIds, Pageable normalPageable);

    long countByPlanCategory(PlanCategory category);

    List<Plan> findByNameContaining(String keyword);
}
