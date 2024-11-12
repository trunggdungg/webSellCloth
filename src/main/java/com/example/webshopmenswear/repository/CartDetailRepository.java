package com.example.webshopmenswear.repository;

import com.example.webshopmenswear.entity.Cart;
import com.example.webshopmenswear.entity.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Integer> {
    List<CartDetail> findByCartId(Integer id);

    CartDetail findByCartAndProductVariantId(Cart cart, Integer productVariant_id);
}
