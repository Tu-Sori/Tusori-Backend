package com.example.tusori_backend.repository;

import com.example.tusori_backend.domain.entity.InterestStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestStockRepository extends JpaRepository<InterestStock, Integer> {
    void deleteByUserUserId(int userId);
}
