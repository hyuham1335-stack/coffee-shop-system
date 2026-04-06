package com.coffeeshopsystem.domain.userpoint.service;

import com.coffeeshopsystem.common.exception.ErrorEnum;
import com.coffeeshopsystem.common.exception.ServiceException;
import com.coffeeshopsystem.domain.userpoint.dto.ChargerUserPointResponse;
import com.coffeeshopsystem.domain.userpoint.entity.PointHistory;
import com.coffeeshopsystem.domain.userpoint.entity.UserPoint;
import com.coffeeshopsystem.domain.userpoint.repository.PointHistoryRepository;
import com.coffeeshopsystem.domain.userpoint.repository.UserPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserPointService {

    private final UserPointRepository userPointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @Transactional
    public ChargerUserPointResponse chargeUserPoint(Long userId, BigDecimal chargeAmount) {

        UserPoint userPoint = userPointRepository.findById(userId)
                .orElseGet(() -> createIfAbsent(userId));

        userPoint.charge(chargeAmount);

        pointHistoryRepository.save(
                PointHistory.charge(chargeAmount, userPoint)
        );

        return ChargerUserPointResponse.of(userPoint);
    }

    // 포인트 충전 중복 요청 발생 시
    // 기존 유저가 아닐 경우 pk 중복 문제 처리
    private UserPoint createIfAbsent(Long userId) {

        try {
            return userPointRepository.save(new UserPoint(userId));
        } catch (DataIntegrityViolationException e) {
            return userPointRepository.findById(userId).orElseThrow(
                    () -> e
            );
        }
    }

    @Transactional
    public void usePoint(Long userId, BigDecimal payAmount) {

        // 비관적락 사용
        UserPoint userPoint = userPointRepository.findByIdWithLock(userId).orElseThrow(
                () -> new ServiceException(ErrorEnum.USER_NOT_FOUND)
        );

        userPoint.pay(payAmount);

        pointHistoryRepository.save(
                PointHistory.use(payAmount, userPoint)
        );
    }
}
