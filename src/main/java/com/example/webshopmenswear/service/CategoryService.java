package com.example.webshopmenswear.service;

import com.example.webshopmenswear.entity.Category;
import com.example.webshopmenswear.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public Category createCategory(Category category) {
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        return categoryRepository.save(category);
    }

    public List<Category> searchCategories(String keyword) {
        return categoryRepository.findByNameContainingIgnoreCase(keyword);
    }

    public Category updateCategory(Integer categoryId, Category updatedCategory) {
        return categoryRepository.findById(categoryId).map(category -> {
            category.setName(updatedCategory.getName());
            category.setSlug(updatedCategory.getSlug());
            category.setUpdatedAt(LocalDateTime.now());
            return categoryRepository.save(category);
        }).orElse(null);
    }

    // Xóa loại sản phẩm
    public boolean deleteCategory(Integer categoryId) {
        if (categoryRepository.existsById(categoryId)) {
            categoryRepository.deleteById(categoryId);
            return true;
        }
        return false;
    }
}

