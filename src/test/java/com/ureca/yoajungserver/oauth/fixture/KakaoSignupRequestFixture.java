package com.ureca.yoajungserver.oauth.fixture;

import com.ureca.yoajungserver.external.kakao.KakaoSignupRequest;
import com.ureca.yoajungserver.user.entity.AgeGroup;
import com.ureca.yoajungserver.user.entity.Gender;

public class KakaoSignupRequestFixture {
    public static final KakaoSignupRequest VALID = KakaoSignupRequest.builder()
            .email("kakao@kakao.com")
            .name("카카오")
            .phoneNumber("01033333333")
            .gender(Gender.FEMALE)
            .ageGroup(AgeGroup.TWENTY_S)
            .familyCount(1)
            .planId(1L)
            .build();
}