package com.ureca.yoajungserver.user.service;

import com.ureca.yoajungserver.user.exception.EmailSendFailedException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendVerificationCode(String email, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("요아정 이메일 인증 코드 ");
            message.setText("인증 코드 : " + code);
            mailSender.send(message);
        } catch (MailException e) {
            throw new EmailSendFailedException();
        }

    }

    @Override
    public void sendPasswordResetLink(String email, String link) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(email);
            helper.setSubject("비밀번호 재설정");
            String html = "<p>비밀번호 재설정 링크 클릭</p>"
                    + "<a href=\"" + link + "\">비밀번호 재설정</a>";
            helper.setText(html, true);
            mailSender.send(message);
        } catch (MessagingException | MailException e) {
            throw new EmailSendFailedException();
        }
    }
}

// 이메일 발송 구현