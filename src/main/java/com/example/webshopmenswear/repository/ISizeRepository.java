package com.example.webshopmenswear.repository;

import com.example.webshopmenswear.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISizeRepository extends JpaRepository<Size, Integer> {
}
