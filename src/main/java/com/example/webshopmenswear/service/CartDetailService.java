package com.example.webshopmenswear.service;

import com.example.webshopmenswear.entity.Cart;
import com.example.webshopmenswear.entity.CartDetail;
import com.example.webshopmenswear.repository.CartDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartDetailService {
    private final CartDetailRepository cartDetailRepository;
    private final CartService cartService;

    public List<CartDetail> getAllProductInCart(Integer userId) {
        // Lấy cart của user hiện tại
        Cart userCart = cartService.getCartByUserId(userId);
        if (userCart == null) {
            return List.of(); // Trả về danh sách rỗng nếu user chưa có cart
        }

        // Lấy tất cả cartDetail theo cart_id
        return cartDetailRepository.findByCartId(userCart.getId());
    }
}
