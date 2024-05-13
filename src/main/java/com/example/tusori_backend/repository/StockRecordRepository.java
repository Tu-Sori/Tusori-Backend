package com.example.tusori_backend.repository;

import com.example.tusori_backend.domain.entity.StockRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRecordRepository extends JpaRepository<StockRecord, Integer> {
    List<StockRecord> findByUserUserIdAndCode(int userId, String code);
    boolean existsByUserUserIdAndCode(int userId, String code);
    void deleteByUserUserId(int userId);
    List<StockRecord> findByUserUserId(int userId);
}
