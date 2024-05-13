package com.example.tusori_backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "interestStock")
public class InterestStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_id")
    private int interestId;

    private String code;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
