package com.ureca.yoajungserver.user.service;

import com.ureca.yoajungserver.user.dto.SignupRequest;
import jakarta.servlet.http.HttpSession;

public interface UserService {
    void signup(SignupRequest request, HttpSession httpSession);
}
