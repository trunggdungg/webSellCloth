package com.example.webshopmenswear.repository;

import com.example.webshopmenswear.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
