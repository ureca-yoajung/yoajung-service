package com.ureca.yoajungserver.user.service;

public interface PasswordResetService {
    void sendResetLink(String email);

    void resetPassword(String token, String newPassword);
}
