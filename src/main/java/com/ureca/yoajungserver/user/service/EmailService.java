package com.ureca.yoajungserver.user.service;

public interface EmailService {
    void sendVerificationCode(String email, String code);

    void sendPasswordResetLink(String email, String resetLink);
}
