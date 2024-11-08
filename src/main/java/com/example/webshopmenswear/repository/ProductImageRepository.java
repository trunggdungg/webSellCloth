package com.example.webshopmenswear.repository;

import com.example.webshopmenswear.entity.Product;
import com.example.webshopmenswear.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    List<ProductImage> findByProductId(Integer id);

    boolean existsByProductAndIsPrimary(Product product, boolean isPrimary);

    List<ProductImage> findByProduct(Product product);
}
