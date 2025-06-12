package com.ureca.yoajungserver.plan.dto.request;

import com.ureca.yoajungserver.plan.entity.PlanCategory;
import com.ureca.yoajungserver.user.entity.AgeGroup;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class PlanFilterRequest {
    private PlanCategory category;

    /** 연령 필터: ALL | TEN_S | TWENTY_S ... */
    private AgeGroup ageGroup;

    /** 정렬: POPULAR | LOW_PRICE | HIGH_PRICE | DATA */
    private String sort;

    /** 데이터 타입: UNLIMITED | FIXED | THROTTLED | ANY(null) */
    private String dataType;

    /** 가격 범위: UNDER5 | BETWEEN6_8 | ABOVE9 | ANY(null) */
    private String priceRange;

    /** 선택된 프로덕트 이름 (없으면 필터 미적용) */
    private Set<String> productNames;
}