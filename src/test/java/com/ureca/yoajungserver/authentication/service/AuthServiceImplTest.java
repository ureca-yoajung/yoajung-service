package com.ureca.yoajungserver.authentication.service;

import com.ureca.yoajungserver.authentication.fixture.AuthFixture;
import com.ureca.yoajungserver.user.service.AuthServiceImpl;
import com.ureca.yoajungserver.user.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DataRedisTest
@Import({AuthServiceImpl.class,
        AuthServiceImplTest.TestConfig.class})
@ActiveProfiles("test")
public class AuthServiceImplTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private EmailService emailService;


    @BeforeEach
    void up() {
        redisTemplate.delete(AuthFixture.REDIS_KEY);
    }

    @Test
    @DisplayName("레디스에 6자리 코드 저장 후 이메일 호출")
    void 인증코드_레디스_저장_이메일_발송() {
        // when
        authService.sendVerificationCode(AuthFixture.EMAIL);

        // then
        String redisCode = redisTemplate.opsForValue().get(AuthFixture.REDIS_KEY);
        assertThat(redisCode).isNotNull().hasSize(6);

        verify(emailService, times(1)).sendVerificationCode(AuthFixture.EMAIL, redisCode);
    }

    @Test
    @DisplayName("올바른 코드면 true 반환하고 레디스 키 삭제")
    void 코드검증_성공_레디스키삭제() {
        // given
        redisTemplate.opsForValue().set(
                AuthFixture.REDIS_KEY, AuthFixture.CODE, Duration.ofMillis(60)
        );

        //when
        boolean result = authService.verifyCode(AuthFixture.EMAIL, AuthFixture.CODE);

        // then
        assertThat(result).isTrue();
        assertThat(redisTemplate.hasKey(AuthFixture.REDIS_KEY)).isFalse();
    }

    @Test
    @DisplayName("인증코드 틀린경우")
    void 코드검증_실패() {
        // given
        redisTemplate.opsForValue().set(
                AuthFixture.REDIS_KEY, "000000", Duration.ofMillis(60)
        );
        // when
        boolean result = authService.verifyCode(AuthFixture.EMAIL, AuthFixture.CODE);
        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("레디스에 키가 없는경우")
    void 키가없네() {
        // when
        boolean result = authService.verifyCode(AuthFixture.EMAIL, AuthFixture.CODE);
        // then
        assertThat(result).isFalse();
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public EmailService emailService() {
            return Mockito.mock(EmailService.class);
        }
    }
}
