package com.example.tusori_backend.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stockRecord")
public class StockRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_record_id")
    private int stock_record_id;

    private boolean sell_or_buy; // 매수/매도 구분

    private String code; // 기업 코드

    private LocalDate sell_or_buy_date; // 매수/매도 일자

    private LocalDate record_date; // 체결 일자

    private int contract_price; // 체결 단가

    private int quantity; // 주문 수량

    private int proceeds; // 수익금

    private float proceeds_rate; // 수익률

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void updateRecordDate(LocalDate date) {
        this.record_date = date;
    }

    public void updateContractPrice(int contract_price) {
        this.contract_price = contract_price;
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }
}
