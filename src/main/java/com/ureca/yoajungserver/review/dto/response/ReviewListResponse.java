package com.ureca.yoajungserver.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewListResponse {
    private Long reviewId;
    private Long userId;
    private String userName;
    private String content;
    private int star;
    private Long likeCnt;
    private LocalDateTime createDate;
    private boolean isAuthor;
}
