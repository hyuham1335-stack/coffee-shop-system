package com.coffeeshopsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CoffeeShopSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoffeeShopSystemApplication.class, args);
    }

}
