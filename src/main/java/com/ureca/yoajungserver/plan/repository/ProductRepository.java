package com.ureca.yoajungserver.plan.repository;

import com.ureca.yoajungserver.plan.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
