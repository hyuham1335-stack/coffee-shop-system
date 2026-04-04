package com.coffeeshopsystem.domain.order.entity;

import com.coffeeshopsystem.common.entity.BaseEntity;
import com.coffeeshopsystem.domain.menu.entity.Menu;
import com.coffeeshopsystem.domain.userpoint.entity.UserPoint;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal totalPrice;


    @JoinColumn(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    public Order(BigDecimal totalPrice, Long userId, Menu menu) {
        this.totalPrice = totalPrice;
        this.userId = userId;
        this.menu = menu;
    }
}
