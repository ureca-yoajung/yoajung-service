package com.ureca.yoajungserver.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmailServiceImplTest {

    @Mock
    JavaMailSender javaMailSender;

    @InjectMocks
    EmailServiceImpl emailService;

    EmailServiceImplTest() {
        MockitoAnnotations.openMocks(this); // Mockito 초기화
    }

    @Test
    @DisplayName("이메일 인증코드 정상 발송")
    void 이메일인증코드발송() {
        String to = "test@t.com";
        String code = "123456";

        emailService.sendVerificationCode(to, code);

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}