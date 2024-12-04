package com.example.webshopmenswear.service;

import com.example.webshopmenswear.entity.OrderDetail;
import com.example.webshopmenswear.entity.Product;
import com.example.webshopmenswear.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public List<OrderDetail> getOrderDetailsByOrderId(Integer orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }

    public List<Product> getTop4MostSoldProducts() {
        Pageable pageable = PageRequest.of(0, 4);  // Trang 0 và 4 sản phẩm mỗi trang
        return orderDetailRepository.findTop4MostSoldProducts(pageable);
    }

}
