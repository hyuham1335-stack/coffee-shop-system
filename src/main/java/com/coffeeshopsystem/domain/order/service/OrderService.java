package com.coffeeshopsystem.domain.order.service;

import com.coffeeshopsystem.domain.order.event.OrderCompletedEvent;
import com.coffeeshopsystem.common.exception.ErrorEnum;
import com.coffeeshopsystem.common.exception.ServiceException;
import com.coffeeshopsystem.domain.menu.entity.Menu;
import com.coffeeshopsystem.domain.menu.repository.MenuRepository;
import com.coffeeshopsystem.domain.order.dto.OrderMenuResponse;
import com.coffeeshopsystem.domain.order.entity.Order;
import com.coffeeshopsystem.domain.order.entity.Payment;
import com.coffeeshopsystem.domain.order.repository.OrderRepository;
import com.coffeeshopsystem.domain.order.repository.PaymentRepository;
import com.coffeeshopsystem.domain.userpoint.service.UserPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final MenuRepository menuRepository;

    private final UserPointService userPointService;

    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public OrderMenuResponse orderMenu(Long userId, Long menuId) {

        Menu menu = menuRepository.findById(menuId).orElseThrow(
                () -> new ServiceException(ErrorEnum.MENU_NOT_FOUND)
        );

        // 비관적락 사용
        userPointService.usePoint(userId, menu.getPrice());

        Order order = orderRepository.save(
                new Order(menu.getPrice(), userId, menu)
        );

        Payment payment = paymentRepository.save(
                new Payment(menu.getPrice(), LocalDateTime.now(), order)
        );

        // 데이터 수집 플랫폼에 전송할 데이터 생성
        eventPublisher.publishEvent(
                new OrderCompletedEvent(
                        order.getId(),
                        order.getMenu().getId(),
                        order.getTotalPrice()
                )
        );

        return OrderMenuResponse.of(order, payment);
    }
}
