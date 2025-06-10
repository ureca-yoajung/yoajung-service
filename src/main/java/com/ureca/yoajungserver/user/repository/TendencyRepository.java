package com.ureca.yoajungserver.user.repository;

import com.ureca.yoajungserver.user.entity.Tendency;
import com.ureca.yoajungserver.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TendencyRepository extends JpaRepository<Tendency, Long> {
    Optional<Tendency> findByUser(User user);
}
