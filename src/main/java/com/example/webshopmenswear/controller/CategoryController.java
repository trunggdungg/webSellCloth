package com.example.webshopmenswear.controller;

import com.example.webshopmenswear.entity.Category;
import com.example.webshopmenswear.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public String getCategoryPage(Model model) {
        List<Category> categories = categoryService.getAllCategory(); // Lấy danh sách category từ service
        model.addAttribute("category", categories); // Gắn danh sách vào model với tên "category"
        return "admin/category/index"; // Trả về template
    }

    @GetMapping("/create")
    public String createProductPage() {
        return "/admin/category/create";
    }
}
