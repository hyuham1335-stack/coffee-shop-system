package com.coffeeshopsystem.domain.ranking.controller;

import com.coffeeshopsystem.common.dto.ApiResponse;
import com.coffeeshopsystem.domain.ranking.dto.SearchPopularMenuResponse;
import com.coffeeshopsystem.domain.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    @GetMapping("/api/menus/popular")
    public ResponseEntity<ApiResponse<List<SearchPopularMenuResponse>>> searchTop3MenusIn7Days () {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                HttpStatus.OK,
                rankingService.searchTop3MenusIn7Days()
        ));
    }
}
