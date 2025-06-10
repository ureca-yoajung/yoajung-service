package com.ureca.yoajungserver.plan.dto.request;

import com.ureca.yoajungserver.plan.entity.PlanCategory;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class PlanFilterRequest {
    private PlanCategory category;
    private Integer minPrice;
    private Integer maxPrice;
    private Boolean unlimited;
    private Set<String> productNames; // OTT, NETFLIX …
}