package com.ureca.yoajungserver.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PlanKeywordResponse {

    private Integer price;

    private String networkType;

    private String callAllowance;

    private String smsAllowance;

    private String dataAllowance;

    private String tetheringSharing;

    private String mediaService;

    private String premiumService;

}
