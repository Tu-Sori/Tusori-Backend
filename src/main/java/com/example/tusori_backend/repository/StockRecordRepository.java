package com.example.tusori_backend.repository;

import com.example.tusori_backend.domain.entity.StockRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRecordRepository extends JpaRepository<StockRecord, Integer> {
    StockRecord findByUserUserIdAndCode(int userId, String code);
    boolean existsByUserUserIdAndCode(int userId, String code);
}
