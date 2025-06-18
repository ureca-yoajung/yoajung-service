package com.ureca.yoajungserver.chatbot.dto;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.stream.Stream;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PlanKeywordResponse {
    private String category;

    private String planTarget;

    private String networkType;

    private String price;

    private String speedAfterLimit;

    private String callAllowance;

    private String smsAllowance;

    private String dataAllowance;

    private String tetheringSharing;

    private String mediaService;

    private String premiumService;

    public boolean hasAnyValidKeyword() {
        return Stream.of(
                category,
                planTarget,
                networkType,
                price,
                speedAfterLimit,
                callAllowance,
                smsAllowance,
                dataAllowance,
                tetheringSharing,
                mediaService,
                premiumService
        ).anyMatch(PlanKeywordResponse::isMeaningful);
    }

    private static boolean isMeaningful(String s) {
        return s != null && !StringUtils.isBlank(s) && !"null".equalsIgnoreCase(s.trim());
    }
}
