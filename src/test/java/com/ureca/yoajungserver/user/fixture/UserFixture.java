package com.ureca.yoajungserver.user.fixture;

import com.ureca.yoajungserver.plan.entity.Plan;
import com.ureca.yoajungserver.user.entity.AgeGroup;
import com.ureca.yoajungserver.user.entity.Gender;
import com.ureca.yoajungserver.user.entity.Role;
import com.ureca.yoajungserver.user.entity.User;

public class UserFixture {
    public static final String EMAIL = "test@test.com";
    public static final String PASSWORD = "pw1234";
    public static final String NAME = "테스트";
    public static final String PHONE = "01011111111";
    public static final Gender GENDER = Gender.MALE;
    public static final AgeGroup AGE_GROUP = AgeGroup.THIRTY_S;
    public static final int FAMILY_COUNT = 3;
    public static final Long PLAN_ID = 1L;

    public static User createUserEntity(String encodedPwd, Plan plan) {
        return User.builder()
                .email(EMAIL)
                .password(encodedPwd)
                .name(NAME)
                .phoneNumber(PHONE)
                .gender(GENDER)
                .ageGroup(AGE_GROUP)
                .familyCount(FAMILY_COUNT)
                .plan(plan)
                .role(Role.USER)
                .build();
    }
}