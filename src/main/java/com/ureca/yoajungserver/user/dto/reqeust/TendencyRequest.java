package com.ureca.yoajungserver.user.dto.reqeust;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TendencyRequest {
    @Setter
    @Min(value = 0, message = "0 이상")
    private Integer avgMonthlyDataGB;
    @Setter
    @Min(value = 0, message = "0 이상")
    private Integer avgMonthlyVoiceMin;
    private String comment;
    @Min(value = 0, message = "0 이상")
    private Integer preferencePrice;

}
