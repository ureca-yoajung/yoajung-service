package com.ureca.yoajungserver.user.fixture;

import com.ureca.yoajungserver.user.dto.reqeust.SignupRequest;
import com.ureca.yoajungserver.user.entity.AgeGroup;
import com.ureca.yoajungserver.user.entity.Gender;
import lombok.Getter;

@Getter
public enum SignupRequestFixture {

    VALID(SignupRequest.builder()
            .name("테스트")
            .email("test@test.com")
            .password("pw1234")
            .planId(1L)
            .phoneNumber("01011111111")
            .gender(Gender.MALE)
            .ageGroup(AgeGroup.THIRTY_S)
            .familyCount(3)
            .build()
    );

    private final SignupRequest request;

    SignupRequestFixture(SignupRequest request) {
        this.request = request;
    }
}