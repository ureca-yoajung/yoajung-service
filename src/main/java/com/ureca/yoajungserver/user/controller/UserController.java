
package com.ureca.yoajungserver.user.controller;

import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.common.BaseCode;
import com.ureca.yoajungserver.user.dto.reqeust.SignupRequest;
import com.ureca.yoajungserver.user.dto.reqeust.UserUpdateRequest;
import com.ureca.yoajungserver.user.dto.response.UserResponse;
import com.ureca.yoajungserver.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody SignupRequest request, HttpSession session) {
        userService.signup(request, session);
        return ResponseEntity.ok(ApiResponse.ok(BaseCode.USER_SIGNUP_SUCCESS));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyInfo(
            @AuthenticationPrincipal UserDetails userDetails) {
        UserResponse response = userService.getUserInfo(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.of(BaseCode.USER_FIND_SUCCESS, response));
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateMyInfo(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UserUpdateRequest request) {

        UserResponse response = userService.updateUserInfo(userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.of(BaseCode.USER_UPDATED_SUCCESS, response));
    }
}