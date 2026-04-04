package com.coffeeshopsystem.domain.userpoint.repository;

import com.coffeeshopsystem.domain.userpoint.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
}
