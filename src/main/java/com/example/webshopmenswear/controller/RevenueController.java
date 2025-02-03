package com.example.webshopmenswear.controller;


import com.example.webshopmenswear.entity.ProductVariant;
import com.example.webshopmenswear.service.ProductVariantService;
import com.example.webshopmenswear.service.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/revenue")
public class RevenueController {
    private final RevenueService revenueService;
    @Autowired
    private ProductVariantService productVariantService;

    @GetMapping
    public String getProductPage(Model model) {
        // Lấy doanh thu
        double revenue = revenueService.calculateRevenue();

        // Gửi dữ liệu doanh thu đến giao diện
        model.addAttribute("revenue", revenue);

        return "/admin/revenue/index";
    }

    @GetMapping("/product")
    public String getRevenueProductPage(Model model) {
        //lay danh sach Product Variant
        List<ProductVariant> productVariants = productVariantService.getAllProductVariant();
        model.addAttribute("productVariants", productVariants);

        return "/admin/revenue/revenueProduct";
    }

    @GetMapping("/monthly")
    public ResponseEntity<Map<String, Object>> getMonthlyRevenue(@RequestParam int month, @RequestParam int year) {
        try {
            Map<String, Object> revenueData = revenueService.getRevenueByMonthAndYear(month, year);
            return ResponseEntity.ok(revenueData);
        } catch (Exception e) {
            // Log lỗi để kiểm tra
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }


}
