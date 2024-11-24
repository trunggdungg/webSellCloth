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

    public void deleteCartDetailById(Integer cartDetailId) {
        if (cartDetailRepository.existsById(cartDetailId)) {
            cartDetailRepository.deleteById(cartDetailId);
        } else {
            throw new RuntimeException("CartDetail not found with id: " + cartDetailId);
        }
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    public CartDetail updateCartItemQuantity(Integer cartDetailId, Integer quantity) {
        CartDetail cartDetail = cartDetailRepository.findById(cartDetailId)
            .orElseThrow(() -> new RuntimeException("CartDetail not found with id: " + cartDetailId));

        // Lấy thông tin sản phẩm và giá gốc
        ProductVariant productVariant = cartDetail.getProductVariant();
        double price = productVariant.getProduct().getPrice();
        Integer discount = productVariant.getProduct().getDiscount();

        // Tính giá đã giảm (nếu có)
        double finalPrice = discount != null
            ? price - (price * discount / 100)
            : price;

        // Cập nhật số lượng và giá trong CartDetail
        cartDetail.setQuantity(quantity);
        cartDetail.setPrice(finalPrice * quantity); // Lưu giá đã giảm nhân số lượng
        return cartDetailRepository.save(cartDetail);
    }

}
