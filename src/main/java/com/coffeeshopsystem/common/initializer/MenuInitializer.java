package com.coffeeshopsystem.common.initializer;

import com.coffeeshopsystem.domain.menu.entity.Menu;
import com.coffeeshopsystem.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Profile("!prod")
@RequiredArgsConstructor
public class MenuInitializer implements CommandLineRunner {

    private final MenuRepository menuRepository;

    @Override
    public void run(String... args) throws Exception {
        Menu menu1 = new Menu(
                "아메리카노",
                BigDecimal.valueOf(4500L)
        );

        Menu menu2 = new Menu(
                "핸드드립 커피",
                BigDecimal.valueOf(6000L)
        );

        Menu menu3 = new Menu(
                "바닐라 라떼",
                BigDecimal.valueOf(5000L)
        );

        Menu menu4 = new Menu(
                "에스프레소",
                BigDecimal.valueOf(5500L)
        );

        menuRepository.save(menu1);
        menuRepository.save(menu2);
        menuRepository.save(menu3);
        menuRepository.save(menu4);
    }
}
