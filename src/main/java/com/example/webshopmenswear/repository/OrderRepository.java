package com.example.webshopmenswear.repository;

import com.example.webshopmenswear.entity.Order;
import com.example.webshopmenswear.model.Enum.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByOrderStatus(OrderStatus orderStatus);


    @Query("SELECT o FROM Order o WHERE o.orderStatus = :orderStatus AND o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findByOrderStatusAndCreatedAtBetween(OrderStatus orderStatus, LocalDateTime startDate, LocalDateTime endDate);

    List<Order> findByUserId(Integer userId);


}
