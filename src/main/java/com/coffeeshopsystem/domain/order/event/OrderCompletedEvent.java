package com.coffeeshopsystem.domain.order.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class OrderCompletedEvent {
    private final Long userId;
    private final Long menuId;
    private final BigDecimal totalPrice;
}
