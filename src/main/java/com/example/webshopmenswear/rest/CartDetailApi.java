package com.example.webshopmenswear.rest;

import com.example.webshopmenswear.entity.CartDetail;
import com.example.webshopmenswear.entity.User;
import com.example.webshopmenswear.model.request.CartDetailRequest;
import com.example.webshopmenswear.service.CartDetailService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartDetailApi {
    private final CartDetailService cartDetailService;
    private final HttpSession httpSession;
    private CartDetail cartDetail;

    @GetMapping("/items")
    public ResponseEntity<List<CartDetail>> getAllProductInCart() {
        User user = (User) httpSession.getAttribute("CURRENT_USER");
        if (user == null) {
            throw new RuntimeException("User not logged in");
        }

        Integer userId = user.getId(); // Lấy userId từ đối tượng User đã lưu trong session
        List<CartDetail> cartDetails = cartDetailService.getAllProductInCart(userId);
        return ResponseEntity.ok(cartDetails);
    }

    // Xóa sản phẩm trong giỏ hàng theo cart_detail_id
    @DeleteMapping("/items/{cartDetailId}")
    public ResponseEntity<Void> deleteProductFromCart(@PathVariable Integer cartDetailId) {
        User user = (User) httpSession.getAttribute("CURRENT_USER");
        if (user == null) {
            throw new RuntimeException("User not logged in");
        }

        Integer userId = user.getId(); // Lấy userId từ đối tượng User đã lưu trong session
        // Kiểm tra nếu cartDetailId là hợp lệ và thuộc về người dùng hiện tại
        cartDetailService.deleteCartDetailById(cartDetailId);
        return ResponseEntity.noContent().build();
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    @PutMapping("/update/{cartDetailId}")
    public ResponseEntity<CartDetail> updateCartItemQuantity(@PathVariable Integer cartDetailId, @RequestBody CartDetailRequest request) {
        User user = (User) httpSession.getAttribute("CURRENT_USER");
        if (user == null) {
            throw new RuntimeException("User not logged in");
        }

        CartDetail updatedCartDetail = cartDetailService.updateCartItemQuantity(cartDetailId, request.getQuantity());
        return ResponseEntity.ok(updatedCartDetail);
    }

}
