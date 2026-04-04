package com.coffeeshopsystem.domain.order.dto;

import com.coffeeshopsystem.domain.order.entity.Order;
import com.coffeeshopsystem.domain.order.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderMenuResponse {

    private Long orderId;
    private final Long userId;
    private final Long menuId;
    private final LocalDateTime paidAt;

    public static OrderMenuResponse of(Order order, Payment payment) {
        return new OrderMenuResponse(
                order.getId(),
                order.getUserId(),
                order.getMenu().getId(),
                payment.getPaidAt()
        );
    }
}
