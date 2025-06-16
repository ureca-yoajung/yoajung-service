package com.ureca.yoajungserver.user.service;

import com.ureca.yoajungserver.user.dto.reqeust.SignupRequest;
import com.ureca.yoajungserver.user.dto.reqeust.UserUpdateRequest;
import com.ureca.yoajungserver.user.dto.response.UserResponse;

public interface UserService {
    void signup(SignupRequest request, String verifiedEmail);

    UserResponse getUserInfo(String username);

    UserResponse updateUserInfo(String username, UserUpdateRequest request);
}
