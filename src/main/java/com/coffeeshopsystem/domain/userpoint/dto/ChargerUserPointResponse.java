package com.coffeeshopsystem.domain.userpoint.dto;

import com.coffeeshopsystem.domain.userpoint.entity.UserPoint;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class ChargerUserPointResponse {
    private final Long userId;
    private final BigDecimal balance;

    public static ChargerUserPointResponse of(UserPoint userPoint) {
        return new ChargerUserPointResponse(userPoint.getUserId(), userPoint.getBalance());
    }
}
