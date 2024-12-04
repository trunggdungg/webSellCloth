package com.example.webshopmenswear.repository;

import com.example.webshopmenswear.entity.Color;
import com.example.webshopmenswear.entity.ProductVariant;
import com.example.webshopmenswear.entity.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT pv " +
            "FROM ProductVariant pv " +
            "WHERE (:colorId IS NULL OR pv.color.id = :colorId) " +
            "AND (:sizeId IS NULL OR pv.size.id = :sizeId) " +
            "AND pv.stock > 0 " +
            "AND (:categoryId IS NULL OR pv.product.category.id = :categoryId)")
// Chỉ lấy những sản phẩm còn hàng và theo loại sản phẩm nếu có
    Page<ProductVariant> findByColorAndSizeAndCategory(
            @Param("colorId") Integer colorId,
            @Param("sizeId") Integer sizeId,
            @Param("categoryId") Integer categoryId,
            Pageable pageable);

    @Query("SELECT pv " +
            "FROM ProductVariant pv " +
            "WHERE (:colorId IS NULL OR pv.color.id = :colorId) " +
            "AND (:sizeId IS NULL OR pv.size.id = :sizeId) " +
            "AND pv.stock > 0")
        // Chỉ lấy những sản phẩm còn hàng
    Page<ProductVariant> findByColorAndSize(
            @Param("colorId") Integer colorId,
            @Param("sizeId") Integer sizeId,
            Pageable pageable);

    @Query("SELECT pv " +
            "FROM ProductVariant pv " +
            "WHERE (:colorId IS NULL OR pv.color.id = :colorId) " +
            "AND pv.stock > 0")
    Page<ProductVariant> findByColor(
            @Param("colorId") Integer colorId,
            Pageable pageable);

    @Query("SELECT pv " +
            "FROM ProductVariant pv " +
            "WHERE (:sizeId IS NULL OR pv.size.id = :sizeId) " +
            "AND pv.stock > 0")
    Page<ProductVariant> findBySize(
            @Param("sizeId") Integer sizeId,
            Pageable pageable);

    @Query("SELECT pv " +
            "FROM ProductVariant pv " +
            "WHERE (:categoryId IS NULL OR pv.product.category.id = :categoryId) " +
            "AND pv.stock > 0")
    Page<ProductVariant> findByCategory(
            @Param("categoryId") Integer categoryId,
            Pageable pageable);

    @Query("SELECT pv " +
            "FROM ProductVariant pv " +
            "WHERE (:colorId IS NULL OR pv.color.id = :colorId) " +
            "AND (:categoryId IS NULL OR pv.product.category.id = :categoryId) " +
            "AND pv.stock > 0")
    Page<ProductVariant> findByColorAndCategory(
            @Param("colorId") Integer colorId,
            @Param("categoryId") Integer categoryId,
            Pageable pageable);

    @Query("SELECT pv " +
            "FROM ProductVariant pv " +
            "WHERE (:sizeId IS NULL OR pv.size.id = :sizeId) " +
            "AND (:categoryId IS NULL OR pv.product.category.id = :categoryId) " +
            "AND pv.stock > 0")
    Page<ProductVariant> findBySizeAndCategory(
            @Param("sizeId") Integer sizeId,
            @Param("categoryId") Integer categoryId,
            Pageable pageable);
}
