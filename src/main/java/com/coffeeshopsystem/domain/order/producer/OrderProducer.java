package com.coffeeshopsystem.domain.order.producer;

import com.coffeeshopsystem.domain.order.event.OrderCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderProducer {

    public void send(OrderCompletedEvent event) {
        log.info("[실시간 데이터 전송] - userId : {}, menuId : {}, totalPrice : {} ", event.getUserId(), event.getMenuId(), event.getTotalPrice());
    }
}
