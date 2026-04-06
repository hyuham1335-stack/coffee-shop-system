package com.coffeeshopsystem.domain.userpoint.entity;

import com.coffeeshopsystem.common.entity.BaseEntity;
import com.coffeeshopsystem.common.exception.ErrorEnum;
import com.coffeeshopsystem.common.exception.ServiceException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "user_points")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPoint extends BaseEntity {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private BigDecimal balance;

    @Version
    private Long version;


    // 기존 사용자가 아닐때 생성자로 생성
    public UserPoint(Long userId) {
        this.userId = userId;
        this.balance = BigDecimal.ZERO;
    }

    public void charge(BigDecimal chargeAmount) {
        this.balance = this.balance.add(chargeAmount);
    }

    public void pay(BigDecimal orderAmount) {
        if (balance.compareTo(orderAmount) < 0) {
            throw new ServiceException(ErrorEnum.INSUFFICIENT_BALANCE);
        }

        this.balance = this.balance.subtract(orderAmount);
    }
}
