package com.ureca.yoajungserver.user.fixture;

import com.ureca.yoajungserver.user.dto.reqeust.UserUpdateRequest;
import com.ureca.yoajungserver.user.entity.AgeGroup;
import com.ureca.yoajungserver.user.entity.Gender;
import lombok.Getter;

@Getter
public enum UserUpdateRequestFixture {

    VALID(UserUpdateRequest.builder()
            .name("테스트")
            .phoneNumber("01022222222")
            .gender(Gender.FEMALE)
            .ageGroup(AgeGroup.FORTY_S)
            .familyCount(2)
            .planId(2L)
            .build()
    ),
    ;

    private final UserUpdateRequest request;

    UserUpdateRequestFixture(UserUpdateRequest request) {
        this.request = request;
    }
}