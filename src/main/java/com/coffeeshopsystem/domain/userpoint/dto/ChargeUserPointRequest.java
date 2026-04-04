package com.coffeeshopsystem.domain.userpoint.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ChargeUserPointRequest {

    @NotNull(message = "사용자 ID는 필수입니다")
    private Long userId;

    @NotNull(message = "충전 금액은 필수입니다")
    @DecimalMin(value = "100", message = "충전 금액은 100원보다 커야 합니다")
    private BigDecimal chargeAmount;
}
