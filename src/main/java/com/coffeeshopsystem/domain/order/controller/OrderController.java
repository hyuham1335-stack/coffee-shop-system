package com.coffeeshopsystem.domain.order.controller;

import com.coffeeshopsystem.common.dto.ApiResponse;
import com.coffeeshopsystem.domain.order.dto.OrderMenuRequest;
import com.coffeeshopsystem.domain.order.dto.OrderMenuResponse;
import com.coffeeshopsystem.domain.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/api/orders")
    public ResponseEntity<ApiResponse<OrderMenuResponse>> orderMenu(
            @RequestBody @Valid OrderMenuRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        HttpStatus.CREATED,
                        orderService.orderMenu(request.getUserId(), request.getMenuId())
                ));
    }
}
