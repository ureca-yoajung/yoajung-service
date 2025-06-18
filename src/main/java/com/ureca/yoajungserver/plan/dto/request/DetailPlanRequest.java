package com.ureca.yoajungserver.plan.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
public class DetailPlanRequest {
    private Long planId;
    private String name;
    private String planCategory;
    private String networkType;
    private Integer basePrice;
    private Integer dataAllowance;
    private Integer tetheringSharingAllowance;
    private Integer speedAfterLimit;
    private String description;
    private String planTarget;
}
