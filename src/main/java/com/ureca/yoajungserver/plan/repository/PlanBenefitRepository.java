package com.ureca.yoajungserver.plan.repository;

import com.ureca.yoajungserver.plan.entity.PlanBenefit;
import com.ureca.yoajungserver.plan.entity.PlanProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlanBenefitRepository extends JpaRepository<PlanBenefit, Long> {
    @Query("select pb from PlanBenefit pb join fetch pb.benefit where pb.plan.id = :planId")
    List<PlanBenefit> findByPlanIdWithBenefit(@Param("planId") Long planId);
}
