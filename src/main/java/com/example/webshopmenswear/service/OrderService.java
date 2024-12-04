package com.example.webshopmenswear.service;

import com.example.webshopmenswear.entity.Order;
import com.example.webshopmenswear.model.Enum.OrderStatus;
import com.example.webshopmenswear.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    public Order getOrderById(Integer orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }

    public String updateOrderStatus(Integer orderId, OrderStatus newStatus) {
        // Lấy đơn hàng theo ID
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return "Order not found";
        }
        // Cập nhật trạng thái đơn hàng
        order.setOrderStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now()); // Cập nhật thời gian thay đổi trạng thái
        // Lưu lại thay đổi vào cơ sở dữ liệu
        orderRepository.save(order);

        return "Order status updated successfully";
    }


    public List<Order> getOrdersByUserId(Integer userId) {
        return orderRepository.findByUserId(userId);
    }
}
