package com.example.webshopmenswear.service;

import com.example.webshopmenswear.entity.Category;
import com.example.webshopmenswear.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    public Category findById(Integer categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }
}

