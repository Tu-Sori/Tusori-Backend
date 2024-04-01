package com.example.tusori_backend.controller;

import com.example.tusori_backend.ApiResponse;
import com.example.tusori_backend.domain.dto.request.SellBuyRequest;
import com.example.tusori_backend.jwt.JwtAuthenticationProvider;
import com.example.tusori_backend.service.SellBuyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/sic")
public class SellBuyController {
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final SellBuyService sellBuyService;

    @PostMapping("buy") // 매수 프로세스
    public ApiResponse<?> buyStrock(@RequestParam("code") String code, @RequestBody SellBuyRequest sellBuyRequest) {
        int userId = jwtAuthenticationProvider.getUserId();
        return ApiResponse.ok(sellBuyService.buyStrock(userId, code, sellBuyRequest));
    }
}
