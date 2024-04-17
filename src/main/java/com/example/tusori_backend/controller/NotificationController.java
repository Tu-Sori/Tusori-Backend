package com.example.tusori_backend.controller;

import com.example.tusori_backend.ApiResponse;
import com.example.tusori_backend.domain.dto.response.NotificationResponse;
import com.example.tusori_backend.jwt.JwtAuthenticationProvider;
import com.example.tusori_backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;


@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final NotificationService notificationService;

    @GetMapping(value = "", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        int userId = jwtAuthenticationProvider.getUserId();
        return notificationService.subscribe(userId);
    }
}
