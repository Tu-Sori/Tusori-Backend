package com.example.tusori_backend.repository;

import com.example.tusori_backend.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
