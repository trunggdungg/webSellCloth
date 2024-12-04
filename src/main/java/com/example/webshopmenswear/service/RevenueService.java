package com.example.webshopmenswear.service;

import com.example.webshopmenswear.entity.Order;
import com.example.webshopmenswear.model.Enum.OrderStatus;
import com.example.webshopmenswear.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RevenueService {
    private final OrderRepository orderRepository;

    public double calculateRevenue() {
        // Lấy tất cả đơn hàng với trạng thái "Đã vận chuyển"
        var completedOrders = orderRepository.findByOrderStatus(OrderStatus.DANG_XU_LY);

        // Tính tổng doanh thu
        return completedOrders.stream()
            .mapToDouble(Order::getTotalPrice)
            .sum();
    }

    public Map<String, Object> getRevenueByMonthAndYear(int month, int year) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<Order> orders = orderRepository.findByOrderStatusAndCreatedAtBetween(
            OrderStatus.DANG_XU_LY, startDate.atStartOfDay(), endDate.atTime(23, 59, 59));

        // Group revenue by day
        Map<Integer, Double> dailyRevenue = orders.stream()
            .collect(Collectors.groupingBy(
                order -> order.getCreatedAt().getDayOfMonth(),
                Collectors.summingDouble(Order::getTotalPrice)
            ));

        // Format result for frontend
        Map<String, Object> result = new HashMap<>();
        result.put("days", dailyRevenue.keySet());
        result.put("revenues", dailyRevenue.values());

        return result;
    }
}
