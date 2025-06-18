package com.ureca.yoajungserver.plan.repository;

import com.ureca.yoajungserver.plan.entity.PlanStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PlanStatisticRepository extends JpaRepository<PlanStatistic, Long> {
    @Query("select sum(ps.userCount) from PlanStatistic ps where ps.planId=:planId")
    Integer popularPlans(@Param("planId")Long planId);
}
