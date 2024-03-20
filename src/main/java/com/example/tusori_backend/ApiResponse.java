package com.example.tusori_backend;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private HttpStatus status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> ok() {
        return (ApiResponse<T>) ApiResponse.builder()
                .status(HttpStatus.OK)
                .message("성공")
                .data(null)
                .build();
    }

    public static <T> ApiResponse<T> ok(T data) {
        return (ApiResponse<T>) ApiResponse.builder()
                .status(HttpStatus.OK)
                .message("성공")
                .data(data)
                .build();
    }
}
