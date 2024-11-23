package com.example.webshopmenswear.controller;

import com.example.webshopmenswear.entity.*;
import com.example.webshopmenswear.model.Enum.OrderStatus;
import com.example.webshopmenswear.repository.AddressRepository;
import com.example.webshopmenswear.repository.CartDetailRepository;
import com.example.webshopmenswear.repository.OrderDetailRepository;
import com.example.webshopmenswear.repository.OrderRepository;
import com.example.webshopmenswear.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final CartDetailRepository cartDetailRepository;
    private final OrderDetailRepository orderDetailRepository;

    @PostMapping("/orders")
    public ResponseEntity<String> createOrder(@RequestBody Order order, HttpSession httpSession) {
        // Lấy người dùng từ session
        User currentUser = (User) httpSession.getAttribute("CURRENT_USER");
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        // Set người dùng cho đơn hàng
        order.setTotalPrice(order.getTotalPrice());
        order.setUser(currentUser);
        order.setOrderStatus(OrderStatus.DANG_XU_LY);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        // Kiểm tra và lấy đối tượng Address từ cơ sở dữ liệu
        if (order.getAddressUser() != null && order.getAddressUser().getId() != null) {
            Optional<Address> addressOptional = addressRepository.findById(order.getAddressUser().getId());
            if (addressOptional.isPresent()) {
                order.setAddressUser(addressOptional.get()); // Gán Address vào Order
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Address not found");
            }
        }

        // Lưu đơn hàng vào cơ sở dữ liệu
        orderRepository.save(order);

        // Lấy danh sách CartDetail từ giỏ hàng của người dùng
        List<CartDetail> cartDetails = cartDetailRepository.findByCart_UserId(currentUser.getId());

        if (cartDetails.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cart is empty");
        }

        // Duyệt qua từng CartDetail để tạo OrderDetail
        for (CartDetail cartDetail : cartDetails) {
            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .variant(cartDetail.getProductVariant())
                    .quantity(cartDetail.getQuantity())
                    .price(cartDetail.getPrice())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            // Lưu OrderDetail vào cơ sở dữ liệu
            orderDetailRepository.save(orderDetail);
        }

        //        // Xóa các CartDetail sau khi chuyển sang OrderDetail (nếu cần)
        cartDetailRepository.deleteAll(cartDetails);
        return ResponseEntity.ok("Order created successfully"); // Trả về 200 OK
    }


    @PutMapping("/{orderId}")
    public Order updateOrderTotalPrice(@PathVariable Integer orderId, @RequestBody Double newTotalPrice) {
        return orderService.updateOrderTotalPrice(orderId, newTotalPrice);
    }
}
