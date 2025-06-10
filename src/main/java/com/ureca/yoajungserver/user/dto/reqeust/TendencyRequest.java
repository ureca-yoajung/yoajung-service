package com.ureca.yoajungserver.user.dto.reqeust;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TendencyRequest {
    @Min(value = 0, message = "0 이상")
    private Integer avgMonthlyDataGB;
    @Min(value = 0, message = "0 이상")
    private Integer avgMonthlyVoiceMin;

    private String comment;

    @Min(value = 0, message = "0 이상")
    private Integer preferencePrice;
}
