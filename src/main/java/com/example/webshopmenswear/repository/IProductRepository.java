package com.example.webshopmenswear.repository;

import com.example.webshopmenswear.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductRepository extends JpaRepository<Product, Integer> {

    Page<Product> findByStatus(Boolean status, Pageable pageable);

    Page<Product> findByNameContainingAndStatus(String name, Boolean status, Pageable pageable);
}
