package com.example.webshopmenswear.entity;

import com.example.webshopmenswear.model.Enum.OrderStatus;
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
@Table(name = "orders")

public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "address_id")
    Address addressUser;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;

    Double totalPrice;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;


}
