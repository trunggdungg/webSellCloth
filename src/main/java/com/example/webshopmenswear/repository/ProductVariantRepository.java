package com.example.webshopmenswear.repository;

import com.example.webshopmenswear.entity.Color;
import com.example.webshopmenswear.entity.ProductVariant;
import com.example.webshopmenswear.entity.Size;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {

    boolean existsByProductIdAndColorIdAndSizeId(Integer id, Integer id1, Integer id2);

    @Query("SELECT DISTINCT pv.color FROM ProductVariant pv WHERE pv.product.id = :productId")
    List<Color> findDistinctColorsByProductId(Integer productId);

    @Query("SELECT DISTINCT pv.size FROM ProductVariant pv WHERE pv.product.id = :productId")
    List<Size> findDistinctSizesByProductId(Integer productId, Sort sort);

    // Lấy stock cho một combination cụ thể
    Optional<ProductVariant> findByProductIdAndColorIdAndSizeId(
        Integer productId, Integer colorId, Integer sizeId);

    List<ProductVariant> findByProductId(Integer productId);
}
