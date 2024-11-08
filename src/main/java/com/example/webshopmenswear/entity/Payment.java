package com.example.webshopmenswear.entity;

import com.example.webshopmenswear.model.Enum.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@ToString
@Builder

@Entity
@Table(name = "payments")

public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @OneToOne
    @JoinColumn(name = "order_id")
    Order order;

    @Column(nullable = false)
    String paymentMethod;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    PaymentStatus paymentStatus;
    String deliveryStatus;
    LocalDateTime paymentDate;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
