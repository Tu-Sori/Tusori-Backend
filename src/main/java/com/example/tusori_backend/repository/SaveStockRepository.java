package com.example.tusori_backend.repository;

import com.example.tusori_backend.domain.entity.SaveStock;
import com.example.tusori_backend.domain.entity.StockRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaveStockRepository extends JpaRepository<SaveStock, Integer> {
    SaveStock findByStockRecord(StockRecord stockRecord);
}
