package com.coffeeshopsystem.domain.ranking.service;

import com.coffeeshopsystem.common.redis.key.RedisKeys;
import com.coffeeshopsystem.domain.menu.entity.Menu;
import com.coffeeshopsystem.domain.menu.repository.MenuRepository;
import com.coffeeshopsystem.domain.ranking.dto.SearchPopularMenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class RankingServiceTest {

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private ZSetOperations<String, String> zSetOperations;

    @InjectMocks
    private RankingService rankingService;

    @BeforeEach
    void setUp() {
        given(stringRedisTemplate.opsForZSet()).willReturn(zSetOperations);
    }

    @Test
    @DisplayName("최근 7일간 인기 메뉴 상위 3개를 정상 조회")
    void searchTop3MenusIn7Days_success() {
        // given
        LocalDate today = LocalDate.now();
        String destKey = RedisKeys.MENU_RANKING_7DAYS + today;

        ZSetOperations.TypedTuple<String> tuple1 =  new DefaultTypedTuple<>("1", 10.0);
        ZSetOperations.TypedTuple<String> tuple2 =  new DefaultTypedTuple<>("2", 7.0);
        ZSetOperations.TypedTuple<String> tuple3 =  new DefaultTypedTuple<>("3", 5.0);

        Set<ZSetOperations.TypedTuple<String>> redisResult = new LinkedHashSet<>();
        redisResult.add(tuple1);
        redisResult.add(tuple2);
        redisResult.add(tuple3);

        Menu menu1 = mock(Menu.class);
        Menu menu2 = mock(Menu.class);
        Menu menu3 = mock(Menu.class);

        given(menu1.getId()).willReturn(1L);
        given(menu2.getId()).willReturn(2L);
        given(menu3.getId()).willReturn(3L);

        given(zSetOperations.unionAndStore(anyString(), anyCollection(), anyString()))
                .willReturn(3L);
        given(zSetOperations.reverseRangeWithScores(destKey, 0, 2))
                .willReturn(redisResult);
        given(menuRepository.findAllById(List.of(1L, 2L, 3L)))
                .willReturn(List.of(menu1, menu2, menu3));

        // when
        List<SearchPopularMenuResponse> result = rankingService.searchTop3MenusIn7Days();

        // then
        assertThat(result).hasSize(3);

        verify(zSetOperations).unionAndStore(anyString(), anyCollection(), eq(destKey));
        verify(zSetOperations).reverseRangeWithScores(destKey, 0, 2);
        verify(menuRepository).findAllById(List.of(1L, 2L, 3L));
    }

    @DisplayName("Redis 결과가 비어있으면 빈 리스트를 반환")
    @Test
    void searchTop3MenusIn7Days_returnsEmptyList_whenRedisResultIsEmpty() {
        // given
        LocalDate today = LocalDate.now();
        String destKey = RedisKeys.MENU_RANKING_7DAYS + today;

        given(zSetOperations.unionAndStore(anyString(), anyCollection(), anyString()))
                .willReturn(0L);
        given(zSetOperations.reverseRangeWithScores(destKey, 0, 2))
                .willReturn(Set.of());

        // when
        List<SearchPopularMenuResponse> result = rankingService.searchTop3MenusIn7Days();

        // then
        assertThat(result).isEmpty();
        verify(menuRepository, never()).findAllById(any());
    }

    @DisplayName("Redis엔 있지만 DB에 없는 메뉴는 결과에서 제외한다")
    @Test
    void searchTop3MenusIn7Days_skipsWhenMenuDoesNotExistInDb() {
        // given
        LocalDate today = LocalDate.now();
        String destKey = RedisKeys.MENU_RANKING_7DAYS + today;

        ZSetOperations.TypedTuple<String> tuple1 = new DefaultTypedTuple<>("1", 10.0);
        ZSetOperations.TypedTuple<String> tuple2 = new DefaultTypedTuple<>("2", 7.0);

        Set<ZSetOperations.TypedTuple<String>> redisResult = new LinkedHashSet<>();
        redisResult.add(tuple1);
        redisResult.add(tuple2);

        Menu menu1 = mock(Menu.class);
        given(menu1.getId()).willReturn(1L);

        given(zSetOperations.unionAndStore(anyString(), anyCollection(), anyString()))
                .willReturn(2L);
        given(zSetOperations.reverseRangeWithScores(destKey, 0, 2))
                .willReturn(redisResult);
        given(menuRepository.findAllById(List.of(1L, 2L)))
                .willReturn(List.of(menu1));

        // when
        List<SearchPopularMenuResponse> result = rankingService.searchTop3MenusIn7Days();

        // then
        assertThat(result).hasSize(1);
        verify(menuRepository).findAllById(List.of(1L, 2L));
    }

    @DisplayName("7일이 넘은 기록은 집계 대상에 포함되지 않는다")
    @Test
    void searchTop3MenusIn7Days_excludesOlderThan7Days() {
        // given
        LocalDate today = LocalDate.now();
        String destKey = RedisKeys.MENU_RANKING_7DAYS + today;

        given(zSetOperations.unionAndStore(anyString(), anyCollection(), anyString()))
                .willReturn(0L);
        given(zSetOperations.reverseRangeWithScores(destKey, 0, 2))
                .willReturn(Collections.emptySet());

        ArgumentCaptor<String> sourceKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Collection<String>> otherKeysCaptor = ArgumentCaptor.forClass(Collection.class);

        // when
        rankingService.searchTop3MenusIn7Days();

        // then
        verify(zSetOperations).unionAndStore(
                sourceKeyCaptor.capture(),
                otherKeysCaptor.capture(),
                eq(destKey)
        );

        String firstKey = sourceKeyCaptor.getValue();
        Collection<String> otherKeys = otherKeysCaptor.getValue();

        String todayKey = RedisKeys.MENU_RANKING_DAILY + today;
        String day1Key = RedisKeys.MENU_RANKING_DAILY + today.minusDays(1);
        String day2Key = RedisKeys.MENU_RANKING_DAILY + today.minusDays(2);
        String day3Key = RedisKeys.MENU_RANKING_DAILY + today.minusDays(3);
        String day4Key = RedisKeys.MENU_RANKING_DAILY + today.minusDays(4);
        String day5Key = RedisKeys.MENU_RANKING_DAILY + today.minusDays(5);
        String day6Key = RedisKeys.MENU_RANKING_DAILY + today.minusDays(6);
        String day7Key = RedisKeys.MENU_RANKING_DAILY + today.minusDays(7);

        assertThat(firstKey).isEqualTo(todayKey);
        assertThat(otherKeys).containsExactly(
                day1Key, day2Key, day3Key, day4Key, day5Key, day6Key
        );
        assertThat(otherKeys).doesNotContain(day7Key);
    }
}