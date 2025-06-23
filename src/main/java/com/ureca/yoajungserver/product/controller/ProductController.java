package com.ureca.yoajungserver.product.controller;

import com.ureca.yoajungserver.common.ApiResponse;
import com.ureca.yoajungserver.common.BaseCode;
import com.ureca.yoajungserver.product.dto.ProductResponse;
import com.ureca.yoajungserver.product.service.ProductService;
import com.ureca.yoajungserver.swagger.api.ProductControllerSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController implements ProductControllerSwagger {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> findAll() {
        return ResponseEntity.ok(
                ApiResponse.of(BaseCode.PRODUCT_LIST_SUCCESS, productService.getAllProducts())
        );
    }
}
