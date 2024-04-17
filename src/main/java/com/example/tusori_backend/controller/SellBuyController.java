package com.example.tusori_backend.controller;

import com.example.tusori_backend.ApiResponse;
import com.example.tusori_backend.domain.dto.request.SellBuyRequest;
import com.example.tusori_backend.jwt.JwtAuthenticationProvider;
import com.example.tusori_backend.service.NotificationService;
import com.example.tusori_backend.service.SellBuyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/sic")
public class SellBuyController {
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final SellBuyService sellBuyService;
    private final NotificationService notificationService;

    @PostMapping("buy") // 매수 프로세스
    public ApiResponse<?> buyStock(@RequestParam("code") String code, @RequestBody SellBuyRequest sellBuyRequest) {
        int userId = jwtAuthenticationProvider.getUserId();
        sellBuyService.buyStock(userId, code, sellBuyRequest);
        notificationService.notify(userId, "매수");
        return ApiResponse.ok();
    }

    @PostMapping("sell") // 매도 프로세스
    public ApiResponse<?> sellStock(@RequestParam("code") String code, @RequestBody SellBuyRequest sellBuyRequest) {
        int userId = jwtAuthenticationProvider.getUserId();
        sellBuyService.sellStock(userId, code, sellBuyRequest);
        notificationService.notify(userId, "매도");
        return ApiResponse.ok();
    }
}
