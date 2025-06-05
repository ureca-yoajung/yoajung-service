package com.ureca.yoajungserver.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {


    @Test
    @DisplayName("redis 저장/메일전송")
    void 인증코드발송성공() {


    }


    @Test
    @DisplayName("세션에 VERIFIED_EMAIL 저장")
    void 코드검증성공() {

    }

    @Test
    @DisplayName("인증코드 만료")
    void 인증코드만료시() {
    }

    @Test
    @DisplayName("인증코드 불일치")
    void 인증코드불일치시() {

    }
}