package com.example.tusori_backend.controller;

import com.example.tusori_backend.ApiResponse;
import com.example.tusori_backend.domain.dto.response.LoginResponse;
import com.example.tusori_backend.jwt.JwtAuthenticationProvider;
import com.example.tusori_backend.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final OAuthService oAuthService;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    @GetMapping("/test")
    public String test() {
        return "testtest";
    }

    @PostMapping("/kakao")
    public ApiResponse<LoginResponse> loginKakao(@RequestParam("code") String authorizationCode) {
        return ApiResponse.ok(oAuthService.loginKakao(authorizationCode));
    }

    @DeleteMapping("")
    public ApiResponse<?> resetUserInfo() {
        int userId = jwtAuthenticationProvider.getUserId();
        return ApiResponse.ok(oAuthService.resetUserInfo(userId));
    }
}