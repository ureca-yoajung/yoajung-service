package com.ureca.yoajungserver.user.dto.response;

import com.ureca.yoajungserver.user.entity.Tendency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TendencyResponse {
    private final Integer avgMonthlyDataGB;
    private final Integer avgMonthlyVoiceMin;
    private final String comment;
    private final Integer preferencePrice;

    public static TendencyResponse fromEntity(Tendency tendency) {
        return TendencyResponse.builder()
                .avgMonthlyDataGB(tendency.getAvgMonthlyDataGB())
                .avgMonthlyVoiceMin(tendency.getAvgMonthlyVoiceMin())
                .comment(tendency.getComment())
                .preferencePrice(tendency.getPreferencePrice())
                .build();
    }
}
