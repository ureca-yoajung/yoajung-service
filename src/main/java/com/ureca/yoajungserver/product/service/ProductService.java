package com.ureca.yoajungserver.product.service;


import com.ureca.yoajungserver.product.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    List<ProductResponse> getAllProducts();
}
