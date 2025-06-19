package com.ureca.yoajungserver.password.service;

import com.ureca.yoajungserver.password.fixture.PasswordResetFixture;
import com.ureca.yoajungserver.user.entity.User;
import com.ureca.yoajungserver.user.exception.PasswordResetTokenInvalidOrExpiredException;
import com.ureca.yoajungserver.user.exception.UserNotFoundException;
import com.ureca.yoajungserver.user.repository.UserRepository;
import com.ureca.yoajungserver.user.service.EmailService;
import com.ureca.yoajungserver.user.service.PasswordResetServiceImpl;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DataRedisTest
@Import({PasswordResetServiceImpl.class,
        PasswordResetServiceImplTest.TestConfig.class})
@ActiveProfiles("test")
public class PasswordResetServiceImplTest {

    @Autowired
    private PasswordResetServiceImpl passwordResetService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void 레디스설정및삭제() {
        Set<String> keys = redisTemplate.keys("password:reset:*");
        keys.forEach(redisTemplate::delete);
    }

    @Test
    @DisplayName("유저없어서 링크못보내")
    void 링크발송_실패_유저없어서_예외() {
        when(userRepository.findByEmail(PasswordResetFixture.EMAIL))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> passwordResetService.sendResetLink(PasswordResetFixture.EMAIL))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("비밀번호 재설정 링크발송성공 및 레디스저장")
    void 링크발송_성공() {
        User user = PasswordResetFixture.user();
        when(userRepository.findByEmail(PasswordResetFixture.EMAIL))
                .thenReturn(Optional.of(user));

        passwordResetService.sendResetLink(PasswordResetFixture.EMAIL);

        Set<String> keys = redisTemplate.keys("password:reset:*");
        // Redis에 해당 토큰 키가 저장되어 있는지 확인
        assertThat(keys).isNotEmpty();

        // EmailService 메일 전송 호출
        verify(emailService, times(1)).sendPasswordResetLink(eq(PasswordResetFixture.EMAIL), anyString());
    }

    @Test
    @DisplayName("비밀번호 재설정 시 토큰이 없거나 만료")
    void 비밀번호재설정_실패_토큰_만료or_없음_예외() {
        assertThatThrownBy(() ->
                passwordResetService.resetPassword(PasswordResetFixture.TOKEN, PasswordResetFixture.PASSWORD)
        ).isInstanceOf(PasswordResetTokenInvalidOrExpiredException.class);
    }

    @Test
    @DisplayName("비밀번호 재설정 후 기존 레디스 키 삭제")
    void resetPassword_success() {

        redisTemplate.opsForValue().set(
                "password:reset:" + PasswordResetFixture.TOKEN,
                PasswordResetFixture.EMAIL,
                Duration.ofMinutes(10)
        );

        User user = Mockito.spy(PasswordResetFixture.user());
        when(userRepository.findByEmail(PasswordResetFixture.EMAIL)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(PasswordResetFixture.PASSWORD)).thenReturn(PasswordResetFixture.ENCODED_PASSWORD);

        passwordResetService.resetPassword(PasswordResetFixture.TOKEN, PasswordResetFixture.PASSWORD);

        verify(user).updatePassword(PasswordResetFixture.ENCODED_PASSWORD);
        assertThat(redisTemplate.hasKey("password:reset:" + PasswordResetFixture.TOKEN)).isFalse();
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserRepository userRepository() {
            return Mockito.mock(UserRepository.class);
        }

        @Bean
        public EmailService emailService() {
            return Mockito.mock(EmailService.class);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return Mockito.mock(PasswordEncoder.class);
        }
    }
}