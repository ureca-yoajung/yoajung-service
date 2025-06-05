package com.ureca.yoajungserver.user.service;

import com.ureca.yoajungserver.common.BaseCode;
import com.ureca.yoajungserver.common.exception.BusinessException;
import com.ureca.yoajungserver.user.dto.reqeust.SendCodeRequest;
import com.ureca.yoajungserver.user.dto.reqeust.VerifyCodeRequest;
import jakarta.servlet.http.HttpSession;
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
    private static final String PENDING_EMAIL = "pendingEmail";
    private static final String VERIFIED_EMAIL = "verifiedEmail";

    @Override
    public void sendVerificationCode(SendCodeRequest request, HttpSession httpSession) {
        String email = request.getEmail();
        String code = String.format("%06d", new Random().nextInt(1_000_000));
        String redisKey = CODE_KEY_PREFIX + email;
        redisTemplate.opsForValue().set(redisKey, code, Duration.ofMillis(codeExpiration));
        // 레디스에 이메일, 인증코드, 만료시간 저장

        httpSession.setAttribute(PENDING_EMAIL, email);
        // 세션키 저장 스프링이 알아서 이메일 저장

        emailService.sendVerificationCode(email, code);
        // 이메일로 발송
    }

    @Override
    public void verifyCode(VerifyCodeRequest request, HttpSession httpSession) {
        Object pending = httpSession.getAttribute(PENDING_EMAIL);
        if (pending == null) {
            throw new BusinessException(BaseCode.INVALID_REQUEST);
        }

        String email = pending.toString();
        String redisKey = CODE_KEY_PREFIX + email;
        String correctCode = redisTemplate.opsForValue().get(redisKey);

        if (correctCode == null) {
            throw new BusinessException(BaseCode.EMAIL_CODE_EXPIRED);
        }// 레디스에서 가져와서 점검 하는데 만료

        if (!correctCode.equals(request.getCode())) {
            throw new BusinessException(BaseCode.EMAIL_CODE_MISMATCH);
        }// 코드 불 일치

        httpSession.setAttribute(VERIFIED_EMAIL, email);
        // 인증 요청한 이메일은 인증으로 세션에 저장
        httpSession.removeAttribute(PENDING_EMAIL);
        redisTemplate.delete(redisKey);
    }
}

// RedisTemplate는 반환타입이 Redis 라서 Optinal로 안 감싸진다.