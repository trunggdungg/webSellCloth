package com.example.webshopmenswear.entity;

import com.example.webshopmenswear.model.Enum.UserRole;
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
@Table(name = "users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false)
    String username;

    @Column(nullable = false)
    String password;

    @Column(nullable = false)
    String email;

    String phoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    UserRole userRole;

    String avatar;

    Boolean isActive;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
