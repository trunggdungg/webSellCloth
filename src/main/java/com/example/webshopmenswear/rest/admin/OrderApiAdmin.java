package com.example.webshopmenswear.rest.admin;


import com.example.webshopmenswear.entity.Order;
import com.example.webshopmenswear.model.Enum.OrderStatus;
import com.example.webshopmenswear.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
public class OrderApiAdmin {
    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/admin/orders/update-status")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(@RequestBody Map<String, Object> request) {
        Integer orderId = Integer.valueOf((String) request.get("orderId"));
        String status = (String) request.get("status");

        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (!orderOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "Order not found"));
        }

        Order order = orderOptional.get();
        switch (status) {
            case "OK" -> {
                if (order.getOrderStatus() == OrderStatus.DANG_XU_LY) {
                    order.setOrderStatus(OrderStatus.DANG_VAN_CHUYEN);
                } else if (order.getOrderStatus() == OrderStatus.DANG_VAN_CHUYEN) {
                    order.setOrderStatus(OrderStatus.DA_VAN_CHUYEN);
                } else if (order.getOrderStatus() == OrderStatus.DA_VAN_CHUYEN) {
                    order.setOrderStatus(OrderStatus.DA_NHAN);
                }
            }
            case "CANCEL" -> order.setOrderStatus(OrderStatus.DA_HUY);
            default -> {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "Invalid status"));
            }
        }
        orderRepository.save(order); // Lưu lại thay đổi trong cơ sở dữ liệu

        return ResponseEntity.ok(Map.of("success", true, "message", "Order status updated"));
    }
}
