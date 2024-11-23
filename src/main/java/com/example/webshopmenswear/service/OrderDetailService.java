package com.example.webshopmenswear.service;

import com.example.webshopmenswear.entity.OrderDetail;
import com.example.webshopmenswear.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;

    // Luu mot Order-detail
    public OrderDetail createOrderDetail(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }

    // Get-ALL
    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailRepository.findAll();
    }

    // Get orderDetail ID
    public OrderDetail getOrderDetailById(Integer orderDetailId) {
        return orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new IllegalArgumentException("OrderDetail not found with ID: " + orderDetailId));
    }

}
