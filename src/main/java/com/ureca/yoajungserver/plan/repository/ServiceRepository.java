package com.ureca.yoajungserver.plan.repository;

import com.ureca.yoajungserver.plan.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Long> {
}
