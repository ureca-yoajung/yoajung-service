package com.ureca.yoajungserver.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final StringRedisTemplate redisTemplate;
    private final EmailService emailService;

    @Value("${spring.email-code-expiration-millis}")
    private long codeExpiration;

    private static final String CODE_KEY_PREFIX = "verify:code:";

    @Override
    public void sendVerificationCode(String email) {
        String code = String.format("%06d", new Random().nextInt(1_000_000));
        String redisKey = CODE_KEY_PREFIX + email;
        redisTemplate.opsForValue().set(redisKey, code, Duration.ofMillis(codeExpiration));
        // 레디스에 이메일, 인증코드, 만료시간 저장

        emailService.sendVerificationCode(email, code);
        // 이메일로 발송
    }

    @Override
    public boolean verifyCode(String email, String code) {
        String redisKey = CODE_KEY_PREFIX + email;
        String correctCode = redisTemplate.opsForValue().get(redisKey);
        // 레디스에서 키값을 조회

        if (correctCode == null) {
            return false;
        }// 레디스에서 가져와서 점검 하는데 만료

        boolean result = correctCode.equals(code);
        if (result) {
            redisTemplate.delete(redisKey);
        }// 코드 일치
        return result;
    }
}