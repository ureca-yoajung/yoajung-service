package com.ureca.yoajungserver.summary.controller;

import com.ureca.yoajungserver.summary.dto.PlanSummaryDto;
import com.ureca.yoajungserver.summary.service.PlanSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.common.BaseCode;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/plans/{id}/summary")
@RequiredArgsConstructor
public class PlanSummaryController {

    private final PlanSummaryService planSummaryService;

    @GetMapping
    public ResponseEntity<ApiResponse<PlanSummaryDto>> getPlanSummary(@PathVariable Long id) {
        PlanSummaryDto dto = planSummaryService.getSummary(id);
        if (dto == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponse.ok(BaseCode.PLAN_SUMMARY_NOT_FOUND));
        }
        return ResponseEntity.ok(
                ApiResponse.of(BaseCode.PLAN_SUMMARY_SUCCESS, dto)
        );
    }
}