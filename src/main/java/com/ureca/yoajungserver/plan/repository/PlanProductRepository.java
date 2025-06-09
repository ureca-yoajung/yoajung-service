package com.ureca.yoajungserver.plan.repository;

import com.ureca.yoajungserver.plan.entity.PlanProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlanProductRepository extends JpaRepository<PlanProduct, Long> {
    @Query("select pp from PlanProduct pp join fetch pp.product where pp.plan.id = :planId")
    List<PlanProduct> findByPlanIdWithProduct(@Param("planId") Long planId);
}
