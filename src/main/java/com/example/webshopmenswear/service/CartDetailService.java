package com.example.webshopmenswear.service;

import com.example.webshopmenswear.entity.Cart;
import com.example.webshopmenswear.entity.CartDetail;
import com.example.webshopmenswear.entity.ProductVariant;
import com.example.webshopmenswear.model.request.CartDetailRequest;
import com.example.webshopmenswear.repository.CartDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartDetailService {
    private final CartDetailRepository cartDetailRepository;
    private final CartService cartService;
    private final ProductVariantService productVariantService;

    public List<CartDetail> getAllProductInCart(Integer userId) {
        // Lấy cart của user hiện tại
        Cart userCart = cartService.getCartByUserId(userId);
        if (userCart == null) {
            return List.of(); // Trả về danh sách rỗng nếu user chưa có cart
        }

        // Lấy tất cả cartDetail theo cart_id
        return cartDetailRepository.findByCartId(userCart.getId());
    }


    public CartDetail addProductToCart(Cart cart, CartDetailRequest request) {
        // Lấy thông tin ProductVariant
        ProductVariant productVariant = productVariantService.getProductVariantById(request.getProductId());

        // Tạo CartDetail mới
        CartDetail newCartDetail = CartDetail.builder()
            .cart(cart)
            .productVariant(productVariant)
            .price(request.getPrice())
            .quantity(request.getQuantity())
            .build();

        // Lưu CartDetail vào database
        return cartDetailRepository.save(newCartDetail);
    }

    public CartDetail save(CartDetail cartDetail) {
        return cartDetailRepository.save(cartDetail);
    }

    public CartDetail findByCartAndProduct(Cart userCart, Integer productId) {
        return cartDetailRepository.findByCartAndProductVariantId(userCart, productId);
    }
}
