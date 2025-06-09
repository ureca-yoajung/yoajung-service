package com.ureca.yoajungserver.user.controller;

import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.common.BaseCode;
import com.ureca.yoajungserver.user.dto.reqeust.TendencyRequest;
import com.ureca.yoajungserver.user.dto.response.TendencyResponse;
import com.ureca.yoajungserver.user.entity.Tendency;
import com.ureca.yoajungserver.user.service.TendencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TendencyController {

    private final TendencyService tendencyService;

    @GetMapping("/api/tendency/me")
    public ResponseEntity<ApiResponse<TendencyResponse>> getMyTendency(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Tendency tendency = tendencyService.getMyTendency(userDetails.getUsername());
        TendencyResponse response = null;

        if (tendency != null) {
            response = TendencyResponse.fromEntity(tendency);
        }
        return ResponseEntity.ok(ApiResponse.of(BaseCode.TENDENCY_FIND_SUCCESS, response));
    }

    @PostMapping("/api/tendency")
    public ResponseEntity<ApiResponse<TendencyResponse>> registerTendency(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody TendencyRequest request) {

        Tendency tendency = tendencyService.registerTendency(userDetails.getUsername(), request);
        TendencyResponse response = TendencyResponse.fromEntity(tendency);
        return ResponseEntity.ok(ApiResponse.of(BaseCode.STATUS_CREATED, response));
    }
}
