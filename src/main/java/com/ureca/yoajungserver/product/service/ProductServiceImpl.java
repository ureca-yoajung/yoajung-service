package com.ureca.yoajungserver.product.service;

import com.ureca.yoajungserver.plan.service.PlanService;
import com.ureca.yoajungserver.product.dto.ProductResponse;
import com.ureca.yoajungserver.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    @Override
    public List<ProductResponse> getAllProducts(){
        return productRepository.findAll()
                .stream()
                .map(ProductResponse::from)
                .toList();
    }
}