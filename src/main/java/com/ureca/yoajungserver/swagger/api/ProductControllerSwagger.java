package com.ureca.yoajungserver.swagger.api;

import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.product.dto.ProductResponse;
import com.ureca.yoajungserver.swagger.response.ApiSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "상품 API", description = "상품 목록 조회 API")
@RequestMapping("/api/products")
public interface ProductControllerSwagger {

    @Operation(
            summary = "상품 목록 조회",
            description = "모든 상품 목록 반환"
    )
    @ApiSuccessResponse(description = "상품 목록 조회 성공")
    @GetMapping
    ResponseEntity<ApiResponse<List<ProductResponse>>> findAll();
}