package com.coffeeshopsystem.domain.order.repository;

import com.coffeeshopsystem.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
