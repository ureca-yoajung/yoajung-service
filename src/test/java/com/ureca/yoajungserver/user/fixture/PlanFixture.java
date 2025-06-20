package com.ureca.yoajungserver.user.fixture;

import com.ureca.yoajungserver.plan.entity.Plan;
import com.ureca.yoajungserver.plan.entity.NetworkType;
import com.ureca.yoajungserver.plan.entity.PlanCategory;
import com.ureca.yoajungserver.plan.entity.PlanTarget;

public class PlanFixture {
    public static final Plan PLAN = Plan.builder()
            .name("스탠다드")
            .networkType(NetworkType.LTE)
            .planTarget(PlanTarget.ALL)
            .planCategory(PlanCategory.LTE_FIVE_G)
            .basePrice(30000)
            .dataAllowance(10)
            .tetheringSharingAllowance(5)
            .speedAfterLimit(1)
            .description("테스트 요금제입니다.")
            .build();
}