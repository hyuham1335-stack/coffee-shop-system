package com.coffeeshopsystem.domain.menu.entity;

import com.coffeeshopsystem.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "menus")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64, nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    public Menu(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }
}
