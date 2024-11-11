package com.example.webshopmenswear.rest;

import com.example.webshopmenswear.entity.CartDetail;
import com.example.webshopmenswear.entity.User;
import com.example.webshopmenswear.service.CartDetailService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartDetailApi {
    private final CartDetailService cartDetailService;
    private final HttpSession httpSession;

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
}
