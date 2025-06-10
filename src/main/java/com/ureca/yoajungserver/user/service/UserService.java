package com.ureca.yoajungserver.user.service;

import com.ureca.yoajungserver.user.dto.reqeust.SignupRequest;
import com.ureca.yoajungserver.user.dto.reqeust.UserUpdateRequest;
import com.ureca.yoajungserver.user.dto.response.UserResponse;
import jakarta.servlet.http.HttpSession;

public interface UserService {
    void signup(SignupRequest request, HttpSession httpSession);

    UserResponse getUserInfo(String username);

    UserResponse updateUserInfo(String username, UserUpdateRequest request);
}
