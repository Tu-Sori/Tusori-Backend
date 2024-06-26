package com.example.tusori_backend.service;

import com.example.tusori_backend.domain.dto.request.SellBuyRequest;
import com.example.tusori_backend.domain.dto.response.SellBuyResponse;
import com.example.tusori_backend.domain.entity.SaveStock;
import com.example.tusori_backend.domain.entity.StockRecord;
import com.example.tusori_backend.domain.entity.User;
import com.example.tusori_backend.repository.SaveStockRepository;
import com.example.tusori_backend.repository.StockRecordRepository;
import com.example.tusori_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SellBuyService {
    private final UserRepository userRepository;
    private final SaveStockRepository saveStockRepository;
    private final StockRecordRepository stockRecordRepository;

    LocalDate date = LocalDate.now(); // 현재 날짜

    // 매수 프로세스
    @Transactional
    public SellBuyResponse buyStock(int userId, String code, SellBuyRequest sellBuyRequest) {
        User user = userRepository.findByUserId(userId);
        boolean possession = stockRecordRepository.existsByUserUserIdAndCode(userId, code); // 주식을 보유하고 있는지 유무

        List<StockRecord> stockRecord = stockRecordRepository.findByUserUserIdAndCode(userId, code);

        // 사용자가 구매한 양
        int purchaseAmount = sellBuyRequest.getPrice() * sellBuyRequest.getAmount();
        int average_purchase = 0, reserves = 0; // 평균매수가, 보유량
        int average_price = 0; // 평단가

        if (possession) { // 기존에 주식을 보유하고 있을 경우
            SaveStock saveStock = saveStockRepository.findByStockRecord(stockRecord.get(0).isSell_or_buy() ? stockRecord.get(0) : stockRecord.get(1)); // 매수매도일지에서 매수에 대한 데이터 가져오기
            // 평균매수가 = (매수한 주식의 수량 * 매수한 가격) / 매수한 주식의 총 수량
            average_purchase = purchaseAmount / (saveStock.getMy_quantity() + sellBuyRequest.getAmount());
            // 보유량 = 기존 가지고 있는 수량 + 추가로 매수하는 수량
            reserves = saveStock.getMy_quantity() + sellBuyRequest.getAmount();

            // 평단가 = ( 보유 주식수 * 보유 주식 매수 단가 ) + ( 신규 매수 주식수 * 신규 매수시 매수가(현재가) ) / ( 보유 주식수 + 신규 매수 주식수 )
            average_price = (saveStock.getMy_quantity() * saveStock.getPurchase()) + (sellBuyRequest.getAmount() * sellBuyRequest.getPrice()) / (saveStock.getMy_quantity() + sellBuyRequest.getAmount());

            saveStock.updatePurchase(sellBuyRequest.getPrice()); // 매입가 변경
            saveStock.updateAveragePrice(average_price); // 평단가 변경
            saveStock.updateQuentity(reserves); // 보유량 변경 (매수량 추가)

            stockRecord.get(0).updateRecordDate(date); // 체결일자 변경
            stockRecord.get(0).updateContractPrice(sellBuyRequest.getPrice()); //체결단가 변경
            stockRecord.get(0).updateQuantity(reserves); // 주문수량 변경

        } else {
            average_purchase = purchaseAmount / sellBuyRequest.getAmount();
            reserves = sellBuyRequest.getAmount();

            // 평단가 = 보유 주식수 * 보유 주식 매수 단가
            average_price = sellBuyRequest.getAmount() * sellBuyRequest.getPrice();

            StockRecord saveStockRecord = stockRecordRepository.save(StockRecord.builder()
                    .sell_or_buy(true) // true -> 매수
                    .code(code) // 기업코드
                    .sell_or_buy_date(date) // 매수/매도 일자
                    .record_date(date) // 체결 일자
                    .contract_price(sellBuyRequest.getPrice()) // 체결단가
                    .quantity(sellBuyRequest.getAmount()) // 주문수량
                    .user(user)
                    .build());

            saveStockRepository.save(SaveStock.builder()
                    .code(code) // 기업코드
                    .purchase(sellBuyRequest.price) // 매입가
                    .average_price(average_price) // 평단가
                    .my_quantity(sellBuyRequest.getAmount()) // 보유수량
                    .stockRecord(saveStockRecord)
                    .build());
        }

        user.changeAssets(user.getAssets() - purchaseAmount); // 가용자산 변경

        return SellBuyResponse.builder()
                .available_assets(user.getAssets() - purchaseAmount) // 가용자산
                .average_purchase(average_purchase) // 평균매수가
                .reserves(reserves) // 보유량
                .build();
    }

    // 매도 프로세스
    @Transactional
    public SellBuyResponse sellStock(int userId, String code, SellBuyRequest sellBuyRequest) {
        User user = userRepository.findByUserId(userId);

        // (1) 사용자가 이 주식을 보유하고 있는지 먼저 체크
        List<StockRecord> stockRecord = stockRecordRepository.findByUserUserIdAndCode(userId, code);
        SaveStock saveStock = saveStockRepository.findByStockRecord(stockRecord.get(0));

        // 수익금 = 매도가격 - 매입가격
        int proceeds = sellBuyRequest.getPrice() - saveStock.getPurchase();
        // 수익률
        int proceeds_rate = proceeds / saveStock.getPurchase() * 100;

        if (saveStock != null) { // 사용자가 주식을 보유하고 있을 경우
            // 매도일지에 먼저 등록
            stockRecordRepository.save(StockRecord.builder()
                            .sell_or_buy(false) // false -> 매도
                            .code(code) // 기업코드
                            .sell_or_buy_date(date) // 매수/매도 일자
                            .record_date(date) // 체결 일자
                            .contract_price(sellBuyRequest.getPrice()) // 체결단가
                            .quantity(sellBuyRequest.getAmount()) // 주문수량
                            .proceeds(proceeds) // 수익금
                            .proceeds_rate(proceeds_rate) // 수익률
                            .user(user)
                            .build());

            // (2) 보유주식 정보 수정
            saveStock.updateQuentity(saveStock.getMy_quantity() - sellBuyRequest.getAmount()); // 보유량 변경
            // 평단가 = 보유 주식수 * 보유 주식 매수 단가
            saveStock.updateAveragePrice(saveStock.getMy_quantity() * saveStock.getPurchase()); // 평단가 변경
        }
        // 사용자가 주식을 보유하고 있지 않을 경우 -> 에러 반환

        // 평균매도가 = ((매도한 주식 수량 * 매도 가격) + (보유 주식 수량 * 보유 주식 평균 매수가)) / 매도한 주식 수량 + 보유 주식 수량

        return SellBuyResponse.builder()
                .available_assets(user.getAssets() + sellBuyRequest.getPrice() * sellBuyRequest.getAmount()) // 가용자산
                .average_purchase(0) // 평균매도가
                .reserves(saveStock.getMy_quantity() - sellBuyRequest.getAmount()) // 보유량
                .build();
    }
}
