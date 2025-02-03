package com.example.webshopmenswear.repository;

import com.example.webshopmenswear.entity.OrderDetail;
import com.example.webshopmenswear.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    @Query("SELECT od FROM OrderDetail od WHERE od.order.id = :orderId")
    List<OrderDetail> findByOrderId(@Param("orderId") Integer orderId);

    @Query("SELECT od.variant.product " +
        "FROM OrderDetail od " +
        "JOIN od.order o " +
        "WHERE o.orderStatus = 'DANG_XU_LY' " +
        "GROUP BY od.variant.product " +
        "ORDER BY SUM(od.quantity) DESC")
    List<Product> findTop4MostSoldProducts(Pageable pageable);
}
