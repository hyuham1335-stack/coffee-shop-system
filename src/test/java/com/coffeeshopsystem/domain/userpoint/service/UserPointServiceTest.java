package com.coffeeshopsystem.domain.userpoint.service;

import com.coffeeshopsystem.domain.userpoint.dto.ChargerUserPointResponse;
import com.coffeeshopsystem.domain.userpoint.entity.PointHistory;
import com.coffeeshopsystem.domain.userpoint.entity.UserPoint;
import com.coffeeshopsystem.domain.userpoint.repository.PointHistoryRepository;
import com.coffeeshopsystem.domain.userpoint.repository.UserPointRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserPointServiceTest {

    @InjectMocks
    private UserPointService userPointService;

    @Mock
    private UserPointRepository userPointRepository;

    @Mock
    private PointHistoryRepository pointHistoryRepository;

    @Test
    @DisplayName("기존 유저-포인트가 있으면 포인트를 충전 후 히스토리를 저장한다")
    void chargeUserPoint_existingUserPoint() {
        // given
        Long userId = 1L;
        BigDecimal chargeAmount = new BigDecimal("1000");

        UserPoint userPoint = new UserPoint(userId);
        userPoint.charge(new BigDecimal("500")); // 기존 포인트 500 보유 상태

        given(userPointRepository.findById(userId)).willReturn(Optional.of(userPoint));
        given(pointHistoryRepository.save(any(PointHistory.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        ChargerUserPointResponse response = userPointService.chargeUserPoint(userId, chargeAmount);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(userId);
        assertThat(response.getBalance()).isEqualByComparingTo("1500");

        verify(userPointRepository).findById(userId);
        verify(pointHistoryRepository).save(any(PointHistory.class));
    }

    @Test
    @DisplayName("유저 포인트가 없으면 새로 생성 후 충전하고 히스토리를 저장한다")
    void chargeUserPoint_newUserPoint() {
        // given
        Long userId = 1L;
        BigDecimal chargeAmount = BigDecimal.valueOf(1000L);

        UserPoint savedUserPoint = new UserPoint(userId);

        given(userPointRepository.findById(userId)).willReturn(Optional.empty());
        given(userPointRepository.save(any(UserPoint.class))).willReturn(savedUserPoint);
        given(pointHistoryRepository.save(any(PointHistory.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        ChargerUserPointResponse response = userPointService.chargeUserPoint(userId, chargeAmount);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(userId);
        assertThat(response.getBalance()).isEqualByComparingTo("1000");

        verify(userPointRepository).findById(userId);
        verify(userPointRepository).save(any(UserPoint.class));
        verify(pointHistoryRepository).save(any(PointHistory.class));
    }

    @Test
    @DisplayName("포인트 충전 시 포인트 이력에 올바른 금액과 유저-포인트를 저장한다")
    void chargeUserPoint_savesCorrectHistory() {
        // given
        Long userId = 1L;
        BigDecimal chargeAmount = BigDecimal.valueOf(3000L);

        UserPoint userPoint = new UserPoint(userId);

        given(userPointRepository.findById(userId)).willReturn(Optional.of(userPoint));
        given(pointHistoryRepository.save(any(PointHistory.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        ArgumentCaptor<PointHistory> historyCaptor = ArgumentCaptor.forClass(PointHistory.class);

        // when
        userPointService.chargeUserPoint(userId, chargeAmount);

        // then
        verify(pointHistoryRepository).save(historyCaptor.capture());

        PointHistory savedHistory = historyCaptor.getValue();

        assertThat(savedHistory).isNotNull();
        assertThat(savedHistory.getAmount()).isEqualByComparingTo("3000");
        assertThat(savedHistory.getUserPoint()).isEqualTo(userPoint);
    }

}