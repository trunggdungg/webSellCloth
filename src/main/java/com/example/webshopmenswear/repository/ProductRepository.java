package com.example.webshopmenswear.repository;

import com.example.webshopmenswear.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Page<Product> findByStatus(Boolean status, Pageable pageable);


    //Search
    Page<Product> findByNameContainingAndStatus(String name, Boolean status, Pageable pageable);

    Page<Product> findTop10ByStatusOrderByCreatedAtDesc(Boolean status, Pageable pageable);

    Product findByIdAndSlugAndStatus(Integer id, String slug, boolean b);

    // List<Product> findTop10ByStatusOrderByCreatedAtDesc(Boolean status);
    // Phương thức tìm sản phẩm có giá trong khoảng
    @Query("SELECT p FROM Product p WHERE p.id <> :productId ORDER BY ABS(p.price - :targetPrice) ASC")
    List<Product> findTop4ByClosestPrice(@Param("targetPrice") Double targetPrice, @Param("productId") Integer productId, Pageable pageable);


    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.id <> :productId AND p.status = true ORDER BY p.createdAt DESC")
    List<Product> findTop4ByCategoryAndStatus(@Param("categoryId") Integer categoryId, @Param("productId") Integer productId);
}
