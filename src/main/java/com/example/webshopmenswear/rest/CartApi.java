package com.example.webshopmenswear.rest;

import com.example.webshopmenswear.entity.Cart;
import com.example.webshopmenswear.entity.CartDetail;
import com.example.webshopmenswear.entity.User;
import com.example.webshopmenswear.model.request.CartDetailRequest;
import com.example.webshopmenswear.service.AuthService;
import com.example.webshopmenswear.service.CartDetailService;
import com.example.webshopmenswear.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartApi {

    private final CartService cartService;
    private final AuthService authService;
    private final CartDetailService cartDetailService;

    @PostMapping("/addCart")
    public ResponseEntity<?> addToCart(@RequestBody CartDetailRequest request, HttpSession httpSession) {
        // Get the current user from the session
        User currentUser = (User) httpSession.getAttribute("CURRENT_USER");

        // Check if the user is not authenticated
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        // Check if the user already has a Cart, if not create a new one
        Cart userCart = cartService.getCartByUserId(currentUser.getId());
        if (userCart == null) {
            userCart = cartService.createCartForUser(currentUser);
        }

        // Check if the product already exists in the cart
        CartDetail existingCartDetail = cartDetailService.findByCartAndProduct(userCart, request.getProductId());

        if (existingCartDetail != null) {
            // Product already exists in the cart, update the quantity
            existingCartDetail.setQuantity(existingCartDetail.getQuantity() + request.getQuantity());
            cartDetailService.save(existingCartDetail);  // Save updated cart detail
        } else {
            // Product does not exist in the cart, create a new CartDetail
            CartDetail newCartDetail = cartDetailService.addProductToCart(userCart, request);
            cartDetailService.save(newCartDetail);  // Save new cart detail
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Product added to cart successfully");
    }


}
