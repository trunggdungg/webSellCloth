package com.example.webshopmenswear.repository;

import com.example.webshopmenswear.entity.Product;
import com.example.webshopmenswear.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    List<ProductImage> findByProductId(Integer id);

    boolean existsByProductAndIsPrimary(Product product, boolean isPrimary);

    ProductImage findByProductAndIsPrimary(Product product, boolean isPrimary);

    // Lấy ảnh đầu tiên dựa theo imageOrder
    ProductImage findFirstByProductIdOrderByImageOrder(Integer productId);

    // Hoặc lấy ảnh primary nếu có
    ProductImage findFirstByProductIdAndIsPrimaryTrue(Integer productId);

    List<ProductImage> findTop2ByProductIdOrderByImageOrder(Integer productId);
}
