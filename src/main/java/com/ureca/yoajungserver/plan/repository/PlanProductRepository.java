package com.ureca.yoajungserver.plan.repository;

import com.ureca.yoajungserver.plan.entity.PlanProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanProductRepository extends JpaRepository<PlanProduct, Long> {
    List<PlanProduct> findByPlanId(Long planId);
}
