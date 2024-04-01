package com.example.tusori_backend.domain.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SellBuyRequest {
    public int price;
    public int amount;
}
