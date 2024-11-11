package com.example.webshopmenswear.service;

import com.example.webshopmenswear.entity.Cart;
import com.example.webshopmenswear.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    public Cart getCartByUserId(Integer userId) {
        return cartRepository.findByUserId(userId);
    }

}
