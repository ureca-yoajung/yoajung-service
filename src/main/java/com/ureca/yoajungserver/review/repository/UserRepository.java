package com.ureca.yoajungserver.review.repository;

import com.ureca.yoajungserver.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 임시 UserRepository. 머지하고 지우기
 */
public interface UserRepository extends JpaRepository<User, Long> {

}
