package com.coffeeshopsystem.domain.userpoint.entity;

import com.coffeeshopsystem.common.exception.ErrorEnum;
import com.coffeeshopsystem.common.exception.ServiceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class UserPointTest {

    @Test
    @DisplayName("잔액이 충분한 경우 결제 후 잔액이 차감된다")
    void pay() {
        // given
        UserPoint userPoint = new UserPoint(1L);
        userPoint.charge(new BigDecimal("5000"));
        BigDecimal orderAmount = new BigDecimal("3000");

        // when
        userPoint.pay(orderAmount);

        // then
        assertThat(userPoint.getBalance()).isEqualByComparingTo("2000");
    }

    @Test
    @DisplayName("잔액이 부족하면 예외가 발생한다")
    void pay_insufficientBalance() {
        // given
        UserPoint userPoint = new UserPoint(1L);
        userPoint.charge(new BigDecimal("1000"));
        BigDecimal orderAmount = new BigDecimal("3000");

        // when & then
        assertThatThrownBy(() -> userPoint.pay(orderAmount))
                .isInstanceOf(ServiceException.class)
                .satisfies(exception -> {
                    ServiceException serviceException = (ServiceException) exception;
                    assertThat(serviceException.getErrorEnum()).isEqualTo(ErrorEnum.INSUFFICIENT_BALANCE);
                });

        assertThat(userPoint.getBalance()).isEqualByComparingTo("1000");
    }
}