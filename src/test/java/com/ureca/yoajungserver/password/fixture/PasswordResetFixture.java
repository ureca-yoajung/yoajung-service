package com.ureca.yoajungserver.password.fixture;

import com.ureca.yoajungserver.user.entity.AgeGroup;
import com.ureca.yoajungserver.user.entity.Gender;
import com.ureca.yoajungserver.user.entity.Role;
import com.ureca.yoajungserver.user.entity.User;

public class PasswordResetFixture {
    public static final String EMAIL = "test@test.com";
    public static final String TOKEN = "test-token";
    public static final String PASSWORD = "pw123";
    public static final String ENCODED_PASSWORD = "ENCODED";

    public static User user() {
        return User.builder()
                .email(EMAIL)
                .password("pw")
                .name("테스트")
                .phoneNumber("01011111111")
                .gender(Gender.MALE)
                .ageGroup(AgeGroup.TWENTY_S)
                .familyCount(1)
                .role(Role.USER)
                .build();
    }
}