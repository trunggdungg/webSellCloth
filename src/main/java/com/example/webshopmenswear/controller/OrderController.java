package com.example.webshopmenswear.controller;

import com.example.webshopmenswear.entity.Order;
import com.example.webshopmenswear.entity.OrderDetail;
import com.example.webshopmenswear.service.OrderDetailService;
import com.example.webshopmenswear.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    @GetMapping
    public String getOrderPage(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());

        return "/admin/order/index";
    }

    @GetMapping("/detail/{id}")
    public String getOrderDetailPage(@PathVariable("id") Integer orderId, Model model) {
        // Lấy thông tin Order theo ID
        Order order = orderService.getOrderById(orderId);
        // Lấy danh sách OrderDetail theo Order ID
        List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(orderId);
        // Truyền dữ liệu vào model để gửi đến view
        model.addAttribute("orderDetails", orderDetails);

        // Trả về template chi tiết đơn hàng
        return "/admin/order/detail";
    }

    @GetMapping("/create")
    public String createOrderPage() {
        return "/admin/order/create";
    }


}
