package com.coffeeshopsystem.domain.userpoint.controller;

import com.coffeeshopsystem.common.dto.ApiResponse;
import com.coffeeshopsystem.domain.userpoint.dto.ChargeUserPointRequest;
import com.coffeeshopsystem.domain.userpoint.dto.ChargerUserPointResponse;
import com.coffeeshopsystem.domain.userpoint.service.UserPointService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PointUserController {

    private final UserPointService userPointService;

    @PostMapping("/api/points/charge")
    public ResponseEntity<ApiResponse<ChargerUserPointResponse>> chargeUserPoint(
            @RequestBody @Valid ChargeUserPointRequest request
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        HttpStatus.CREATED,
                        userPointService.chargeUserPoint(request.getUserId(), request.getChargeAmount())
                ));
    }
}
