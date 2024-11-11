package com.example.webshopmenswear.entity;

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
@Table(name = "order_details")

public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    Order order;

    @ManyToOne
    @JoinColumn(name = "product_variant_id")
    ProductVariant variant;

    Integer quantity;

    Double price;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
