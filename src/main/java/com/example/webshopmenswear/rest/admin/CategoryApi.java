package com.example.webshopmenswear.rest.admin;

import com.example.webshopmenswear.entity.Category;
import com.example.webshopmenswear.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class CategoryApi {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        return ResponseEntity.ok(categoryService.createCategory(category));
    }


    @GetMapping("/search_category")
    public ResponseEntity<List<Category>> searchCategories(@RequestParam String keyword) {
        List<Category> categories = categoryService.searchCategories(keyword);
        return ResponseEntity.ok(categories);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id) {
        if (categoryService.deleteCategory(id)) {
            return ResponseEntity.ok("Deleted successfully");
        }
        return ResponseEntity.badRequest().body("Category not found");
    }
}
