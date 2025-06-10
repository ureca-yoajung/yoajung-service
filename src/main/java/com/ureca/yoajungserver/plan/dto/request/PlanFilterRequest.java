package com.ureca.yoajungserver.plan.dto.request;

import com.ureca.yoajungserver.plan.entity.PlanCategory;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class PlanFilterRequest {
    private PlanCategory category;

    /** 데이터 타입: UNLIMITED | FIXED | THROTTLED | ANY(null) */
    private String dataType;

    /** 가격 범위: UNDER5 | BETWEEN6_8 | ABOVE9 | ANY(null) */
    private String priceRange;

    /** 선택된 프로덕트 이름 (없으면 필터 미적용) */
    private Set<String> productNames;
}