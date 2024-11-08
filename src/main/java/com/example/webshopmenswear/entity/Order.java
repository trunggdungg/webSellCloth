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

    @Column(nullable = false)
    String receiverName;

    @Column(nullable = false)
    String phoneNumber;

    String email;

    String addressReceiver;

    @ManyToOne
    @JoinColumn(name = "address_id")
    Address addressUser;

    @ManyToOne
    @JoinColumn(name = "ward_id")
    Ward ward;

    @ManyToOne
    @JoinColumn(name = "district_id")
    District district;

    @ManyToOne
    @JoinColumn(name = "province_id")
    Province province;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;

    Double totalPrice;

    LocalDateTime createdAt;


}
