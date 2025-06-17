package com.ureca.yoajungserver.user.controller;

import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.user.dto.reqeust.TendencyRequest;
import com.ureca.yoajungserver.user.dto.response.TendencyResponse;
import com.ureca.yoajungserver.user.entity.Tendency;
import com.ureca.yoajungserver.user.service.TendencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.ureca.yoajungserver.common.BaseCode.*;

@RestController
@RequestMapping("/api/tendency")
@RequiredArgsConstructor
public class TendencyController {

    private final TendencyService tendencyService;

    @GetMapping
    public ResponseEntity<ApiResponse<TendencyResponse>> getMyTendency(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Optional<Tendency> tendency = tendencyService.getMyTendency(userDetails.getUsername());

        TendencyResponse response =
                tendency.map(TendencyResponse::fromEntity)
                        .orElse(null);
        return ResponseEntity.ok(ApiResponse.of(TENDENCY_FIND_SUCCESS, response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TendencyResponse>> registerTendency(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody TendencyRequest request) {

        Tendency tendency = tendencyService.registerTendency(userDetails.getUsername(), request);
        TendencyResponse response = TendencyResponse.fromEntity(tendency);
        return ResponseEntity.ok(ApiResponse.of(TENDENCY_CREATED_SUCCESS, response));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<TendencyResponse>> updateTendency(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody TendencyRequest request) {
        Tendency tendency = tendencyService.updateTendency(userDetails.getUsername(), request);
        TendencyResponse response = TendencyResponse.fromEntity(tendency);
        return ResponseEntity.ok(ApiResponse.of(TENDENCY_UPDATED_SUCCESS, response));
    }
}
