package com.coffeeshopsystem.domain.userpoint.service;

import com.coffeeshopsystem.common.exception.ErrorEnum;
import com.coffeeshopsystem.common.exception.ServiceException;
import com.coffeeshopsystem.domain.userpoint.dto.ChargerUserPointResponse;
import com.coffeeshopsystem.domain.userpoint.entity.PointHistory;
import com.coffeeshopsystem.domain.userpoint.entity.UserPoint;
import com.coffeeshopsystem.domain.userpoint.repository.PointHistoryRepository;
import com.coffeeshopsystem.domain.userpoint.repository.UserPointRepository;
import lombok.RequiredArgsConstructor;
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
                .orElseGet(() -> userPointRepository.save(new UserPoint(userId)));

        userPoint.charge(chargeAmount);

        pointHistoryRepository.save(
                PointHistory.charge(chargeAmount, userPoint)
        );

        return ChargerUserPointResponse.of(userPoint);
    }

    @Transactional
    public void usePoint(Long userId, BigDecimal payAmount) {

        UserPoint userPoint = userPointRepository.findById(userId).orElseThrow(
                () -> new ServiceException(ErrorEnum.USER_NOT_FOUND)
        );

        userPoint.pay(payAmount);

        pointHistoryRepository.save(
                PointHistory.use(payAmount, userPoint)
        );
    }
}
