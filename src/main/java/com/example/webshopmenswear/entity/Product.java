package com.example.webshopmenswear.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@ToString
@Builder

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String slug;

    String description;

    @Column(nullable = false)
    Double price;

    Boolean status;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
        name = "product_discount",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "discount_id")
    )
    List<Discount> discounts;

}


