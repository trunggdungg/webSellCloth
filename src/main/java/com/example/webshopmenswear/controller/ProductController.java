package com.example.webshopmenswear.controller;

import com.example.webshopmenswear.service.CategoryService;
import com.example.webshopmenswear.service.ProductImageService;
import com.example.webshopmenswear.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/product")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ProductImageService productImageService;


    @GetMapping
    public String getProductPage(Model model) {
        model.addAttribute("products", productService.getAllProduct());
        return "/admin/product/index";
    }

    @GetMapping("/create")
    public String createProductPage(Model model) {
        model.addAttribute("categories", categoryService.getAllCategory());
        return "/admin/product/create";
    }

    @GetMapping("/detail/{id}")
    public String getProductDetailPage(@PathVariable Integer id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("images", productImageService.getListImagesProduct(id));
        return "/admin/product/detail";
    }
}
