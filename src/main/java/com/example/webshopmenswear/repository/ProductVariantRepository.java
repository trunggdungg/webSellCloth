package com.example.webshopmenswear.repository;

import com.example.webshopmenswear.entity.Color;
import com.example.webshopmenswear.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {

    boolean existsByProductIdAndColorIdAndSizeId(Integer id, Integer id1, Integer id2);

    @Query("SELECT DISTINCT pv.color FROM ProductVariant pv WHERE pv.product.id = :productId")
    List<Color> findDistinctColorsByProductId(Integer productId);

    @Query("SELECT DISTINCT pv.size FROM ProductVariant pv WHERE pv.product.id = :productId")
    List<Color> findsize(Integer productId);
}
