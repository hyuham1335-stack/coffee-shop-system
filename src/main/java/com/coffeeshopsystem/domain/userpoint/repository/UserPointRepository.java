package com.coffeeshopsystem.domain.userpoint.repository;

import com.coffeeshopsystem.domain.userpoint.entity.UserPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPointRepository extends JpaRepository<UserPoint, Long> {
}
