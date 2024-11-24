package com.example.webshopmenswear.controller;

import com.example.webshopmenswear.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public String getOrderPage(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());

        return "/admin/order/index";
    }

    @GetMapping("/detail/{id}")
    public String getOrderDetailPage() {
        return "/admin/order/detail";
    }

    @GetMapping("/create")
    public String createOrderPage() {
        return "/admin/order/create";
    }

}
