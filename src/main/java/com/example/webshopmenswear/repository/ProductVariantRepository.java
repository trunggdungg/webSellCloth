package com.example.webshopmenswear.repository;

import com.example.webshopmenswear.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {

    boolean existsByProductIdAndColorIdAndSizeId(Integer id, Integer id1, Integer id2);
}
