package com.coffeeshopsystem.domain.ranking.service;

import com.coffeeshopsystem.common.redis.key.RedisKeys;
import com.coffeeshopsystem.domain.menu.entity.Menu;
import com.coffeeshopsystem.domain.menu.repository.MenuRepository;
import com.coffeeshopsystem.domain.ranking.dto.SearchPopularMenuResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final StringRedisTemplate stringRedisTemplate;
    private final MenuRepository menuRepository;

    public void increaseMenuRanking(Long menuId, LocalDate now) {

        String key = RedisKeys.MENU_RANKING_DAILY + now;

        stringRedisTemplate.opsForZSet().incrementScore(key, String.valueOf(menuId), 1);
    }

    public List<SearchPopularMenuResponse> searchTop3MenusIn7Days() {

        LocalDate standardDate = LocalDate.now();

        List<String> keys = List.of(
                RedisKeys.MENU_RANKING_DAILY + standardDate,
                RedisKeys.MENU_RANKING_DAILY + standardDate.minusDays(1),
                RedisKeys.MENU_RANKING_DAILY + standardDate.minusDays(2),
                RedisKeys.MENU_RANKING_DAILY + standardDate.minusDays(3),
                RedisKeys.MENU_RANKING_DAILY + standardDate.minusDays(4),
                RedisKeys.MENU_RANKING_DAILY + standardDate.minusDays(5),
                RedisKeys.MENU_RANKING_DAILY + standardDate.minusDays(6)
        );

        String destKey = RedisKeys.MENU_RANKING_7DAYS + standardDate;

        stringRedisTemplate.opsForZSet().unionAndStore(
                keys.get(0),
                keys.subList(1, keys.size()),
                destKey
        );

        Set<ZSetOperations.TypedTuple<String>> result = stringRedisTemplate.opsForZSet()
                .reverseRangeWithScores(destKey, 0, 2);

        if (result == null || result.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> menuIds = result.stream()
                .map(ZSetOperations.TypedTuple::getValue)
                .filter(Objects::nonNull)
                .map(Long::valueOf)
                .toList();

        List<Menu> menus = menuRepository.findAllById(menuIds);

        Map<Long, Menu> menuMap = menus.stream()
                .collect(Collectors.toMap(Menu::getId, Function.identity()));

        return result.stream()
                .map(tuple -> {

                    String value = tuple.getValue();
                    if(value == null) {
                        return null;
                    }

                    Long menuId = Long.valueOf(value);

                    Menu menu = menuMap.get(menuId);
                    if(menu == null) {
                        return null;
                    }

                    Double score = tuple.getScore();
                    if(score == null) {
                        return null;
                    }

                    return SearchPopularMenuResponse.of(menu, score);
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
