package com.coffeeshopsystem.domain.order.producer;

import com.coffeeshopsystem.domain.order.event.OrderCompletedEvent;
import com.coffeeshopsystem.domain.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderProducer {

    private final RankingService rankingService;

    public void send(OrderCompletedEvent event) {
        log.info("[실시간 데이터 전송] - userId : {}, menuId : {}, totalPrice : {} ", event.getUserId(), event.getMenuId(), event.getTotalPrice());

        LocalDate now = LocalDate.now();

        rankingService.increaseMenuRanking(event.getMenuId(), now);
    }
}
