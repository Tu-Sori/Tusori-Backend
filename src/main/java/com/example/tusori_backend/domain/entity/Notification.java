package com.example.tusori_backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private int notificationId;

    private String content; // 내용

    private LocalDate createdAt; // 생성일자

    @Column(nullable = false)
    private boolean isRead; // 읽었는지 유무
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void updateRead() {
        this.isRead = true;
    }
}
