package com.example.tusori_backend.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "saveStock")
public class SaveStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int stock_id;

    private String code; // 기업코드

    private int purchase; // 매입가

    private int average_price; // 평단가

    private int my_quantity; // 보유수량


    @ManyToOne
    @JoinColumn(name = "stock_record_id")
    private StockRecord stockRecord;

    public void updatePurchase(int purchase) {
        this.purchase = purchase;
    }

    public void updateAveragePrice(int average_price) {
        this.average_price = average_price;
    }

    public void updateQuentity(int quentity) {
        this.my_quantity = quentity;
    }
}
