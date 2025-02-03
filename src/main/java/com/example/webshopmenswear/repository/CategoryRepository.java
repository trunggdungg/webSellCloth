package com.example.webshopmenswear.repository;

import com.example.webshopmenswear.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(String name);

    List<Category> findByNameContainingIgnoreCase(String keyword);
}
