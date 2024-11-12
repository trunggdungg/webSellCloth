package com.example.webshopmenswear.service;

import com.example.webshopmenswear.entity.Cart;
import com.example.webshopmenswear.entity.User;
import com.example.webshopmenswear.repository.CartDetailRepository;
import com.example.webshopmenswear.repository.CartRepository;
import com.example.webshopmenswear.repository.ProductVariantRepository;
import com.example.webshopmenswear.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductVariantRepository productVariantRepository;
    private final CartDetailRepository cartDetailRepository;

    public Cart getCartByUserId(Integer userId) {
        return cartRepository.findByUserId(userId);
    }


    public Cart createCartForUser(User user) {
        Cart newCart = Cart.builder()
            .user(user)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        return cartRepository.save(newCart);
    }

}
