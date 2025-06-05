
package com.ureca.yoajungserver.user.controller;

import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.common.BaseCode;
import com.ureca.yoajungserver.user.dto.reqeust.SignupRequest;
import com.ureca.yoajungserver.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/api/user/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody SignupRequest request, HttpSession session) {
        userService.signup(request, session);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(BaseCode.USER_SIGNUP_SUCCESS));
    }
}