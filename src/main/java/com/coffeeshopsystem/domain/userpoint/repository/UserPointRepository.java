package com.coffeeshopsystem.domain.userpoint.repository;

import com.coffeeshopsystem.domain.userpoint.entity.UserPoint;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserPointRepository extends JpaRepository<UserPoint, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT up FROM UserPoint up WHERE up.userId = :userId")
    Optional<UserPoint> findByIdWithLock(@Param("userId") Long userId);
}
