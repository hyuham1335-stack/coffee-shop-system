package com.coffeeshopsystem.domain.ranking.dto;

import com.coffeeshopsystem.domain.menu.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class SearchPopularMenuResponse {
    private final Long menuId;
    private final String name;
    private final BigDecimal price;
    private final double orderCount;

    public static SearchPopularMenuResponse of(Menu menu, Double orderCount) {
        return new SearchPopularMenuResponse(menu.getId(), menu.getName(), menu.getPrice(), orderCount);
    }
}
