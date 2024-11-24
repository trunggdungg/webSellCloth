package com.example.webshopmenswear.service;

import com.example.webshopmenswear.entity.Order;
import com.example.webshopmenswear.model.Enum.OrderStatus;
import com.example.webshopmenswear.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {  // Sửa tên class từ 'OderService' thành 'OrderService'

    private final OrderRepository orderRepository;


    public Order createOrder(Order order) {
        // Thiết lập thời gian tạo đơn hàng

        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        // Thiết lập trạng thái đơn hàng mặc định là PENDING nếu không có giá trị
        if (order.getOrderStatus() == null) {
            order.setOrderStatus(OrderStatus.DANG_XU_LY);
        }

        // Lưu đơn hàng vào cơ sở dữ liệu và trả về đối tượng đơn hàng đã được lưu
        return orderRepository.save(order);
    }

    public Order updateOrderTotalPrice(Integer orderId, Double newTotalPrice) {
        // Tìm đơn hàng theo ID
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        // Cập nhật giá trị tổng đơn hàng mới
        order.setTotalPrice(newTotalPrice);
        order.setUpdatedAt(LocalDateTime.now());  // Cập nhật thời gian thay đổi

        // Lưu đơn hàng đã được cập nhật
        return orderRepository.save(order);
    }

    public Object getAllOrders() {
        return orderRepository.findAll(Sort.by(Sort.Direction.DESC, "CreatedAt"));
    }
}
