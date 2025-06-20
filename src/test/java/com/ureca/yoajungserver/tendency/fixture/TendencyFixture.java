package com.ureca.yoajungserver.tendency.fixture;

import com.ureca.yoajungserver.user.entity.Tendency;
import com.ureca.yoajungserver.user.entity.User;

public class TendencyFixture {
    public static Tendency create(User user) {
        return Tendency.builder()
                .user(user)
                .avgMonthlyDataGB(10)
                .avgMonthlyVoiceMin(200)
                .comment("많이 써요")
                .preferencePrice(20000)
                .build();
    }
}