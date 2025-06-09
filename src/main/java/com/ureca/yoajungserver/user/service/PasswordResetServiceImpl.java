package com.ureca.yoajungserver.user.service;

import com.ureca.yoajungserver.user.entity.User;
import com.ureca.yoajungserver.user.exception.PasswordResetTokenInvalidOrExpiredException;
import com.ureca.yoajungserver.user.exception.UserNotFoundException;
import com.ureca.yoajungserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {
    private final UserRepository userRepository;
    private final StringRedisTemplate redisTemplate;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${spring.password-reset-token-expiration-millis}")
    private long expirationMillis;

    private static final String PASSWORD_RESET_KEY_PREFIX = "password:reset:";

    @Override
    public void sendResetLink(String email) {

       /*
       troubleshooting
       링크 요청을 여러번할 시 이전에 남아있는 토큰들은 다 지워져야 한다.
        */

        Set<String> keys = redisTemplate.keys(PASSWORD_RESET_KEY_PREFIX + "*");
        if (!keys.isEmpty()) {
            for (String key : keys) {
                String value = redisTemplate.opsForValue().get(key);
                if (email.equals(value)) {
                    redisTemplate.delete(key);
                }
            }
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        String token = UUID.randomUUID().toString();

        String redisKey = PASSWORD_RESET_KEY_PREFIX + token;
        redisTemplate.opsForValue().set(redisKey, email, Duration.ofMillis(expirationMillis));

        String resetLink = "http://localhost:8080/reset-password.html?token=" + token;

        emailService.sendPasswordResetLink(email, resetLink);
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        String redisKey = PASSWORD_RESET_KEY_PREFIX + token;
        String email = redisTemplate.opsForValue().get(redisKey);

        if (email == null) {
            throw new PasswordResetTokenInvalidOrExpiredException();
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        user.updatePassword(passwordEncoder.encode(newPassword));
        redisTemplate.delete(redisKey);
    }
}
