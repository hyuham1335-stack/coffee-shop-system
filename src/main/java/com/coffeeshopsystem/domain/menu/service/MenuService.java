package com.coffeeshopsystem.domain.menu.service;

import com.coffeeshopsystem.domain.menu.dto.SearchMenuResponse;
import com.coffeeshopsystem.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    @Transactional(readOnly = true)
    public List<SearchMenuResponse> SearchMenus() {
        return menuRepository.findAll().stream()
                .map(SearchMenuResponse::of)
                .toList();
    }
}
