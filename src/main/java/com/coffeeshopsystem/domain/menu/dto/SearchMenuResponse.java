package com.coffeeshopsystem.domain.menu.dto;

import com.coffeeshopsystem.domain.menu.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class SearchMenuResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;

    public static SearchMenuResponse of(Menu menu) {
        return new SearchMenuResponse(menu.getId(), menu.getName(), menu.getPrice());
    }
}
