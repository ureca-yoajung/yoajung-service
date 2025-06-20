package com.ureca.yoajungserver.email.service;

import com.ureca.yoajungserver.email.fixture.EmailFixture;
import com.ureca.yoajungserver.user.exception.EmailSendFailedException;
import com.ureca.yoajungserver.user.service.EmailServiceImpl;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceImpTest {
    @InjectMocks
    private EmailServiceImpl emailService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "fromEmail", EmailFixture.FROM);
    }

    @Test
    @DisplayName("이메일 인증코드 발송 성공")
    void 인증코드_발송_성공() {
        // when
        emailService.sendVerificationCode(EmailFixture.EMAIL, EmailFixture.CODE);
        // then
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("이메일 인증코드 발송 실패시 예외 던짐")
    void 인증코드_발송_실패() {
        // given
        doThrow(new MailException("fail") {
        }).when(mailSender).send(any(SimpleMailMessage.class));

        // when then
        assertThatThrownBy(() -> emailService.sendVerificationCode(EmailFixture.EMAIL, EmailFixture.CODE))
                .isInstanceOf(EmailSendFailedException.class);
    }

    @Test
    @DisplayName("비밀번호 재설정 링크 메일 발송 성공")
    void 재설정링크_발송_성공() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendPasswordResetLink(EmailFixture.EMAIL, EmailFixture.LINK);

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("비밀번호 재설정 링크 발송 실패 시 예외 발생")
    void 재설정링크_빨송_실패() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        doThrow(new MailException("메일 발송 실패") {
        }).when(mailSender).send(any(MimeMessage.class));

        assertThatThrownBy(() -> emailService.sendPasswordResetLink(EmailFixture.EMAIL, EmailFixture.LINK))
                .isInstanceOf(EmailSendFailedException.class);
    }
}
