package com.example.tusori_backend.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SellBuyResponse {
    private int available_assets; // 가용자산
    private int average_purchase; // 평균매수가
    private int reserves; // 보유량
}
