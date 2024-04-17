package com.example.tusori_backend.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private String content;
    private boolean isRead;
    private String createdAt;
}