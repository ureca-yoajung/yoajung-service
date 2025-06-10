package com.ureca.yoajungserver.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewPageResponse {
    private List<ReviewListResponse> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private Double avgStar;
    private Boolean isPlanUser;
    private Boolean isLogined;

    public ReviewPageResponse(Page<ReviewListResponse> pageData, Double avgStar, Boolean isPlanUser, Boolean isLogined) {
        this.content = pageData.getContent();
        this.page = pageData.getNumber();
        this.size = pageData.getSize();
        this.totalElements = pageData.getTotalElements();
        this.totalPages = pageData.getTotalPages();
        this.avgStar = avgStar;
        this.isPlanUser = isPlanUser;
        this.isLogined = isLogined;
    }
}
