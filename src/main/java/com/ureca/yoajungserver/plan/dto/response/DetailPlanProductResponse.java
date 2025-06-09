package com.ureca.yoajungserver.plan.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DetailPlanProductResponse {
    private List<DetailProductDto> media;
    private List<DetailProductDto> premium;
}
