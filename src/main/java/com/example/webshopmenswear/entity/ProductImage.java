package com.example.webshopmenswear.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@ToString
@Builder

@Entity
@Table(name = "product_images")

public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

    @Column(nullable = false)
    String imageUrl;

  
    String altText;

    Boolean isPrimary;

    Integer imageOrder;


}
