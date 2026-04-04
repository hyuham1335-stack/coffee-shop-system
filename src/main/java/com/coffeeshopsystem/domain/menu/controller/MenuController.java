package com.coffeeshopsystem.domain.menu.controller;

import com.coffeeshopsystem.common.dto.ApiResponse;
import com.coffeeshopsystem.domain.menu.dto.SearchMenuResponse;
import com.coffeeshopsystem.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/api/menus")
    public ResponseEntity<ApiResponse<List<SearchMenuResponse>>> SearchMenus() {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK, menuService.SearchMenus()));
    }

}
