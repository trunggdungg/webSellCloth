package com.example.webshopmenswear.rest;

import com.example.webshopmenswear.entity.*;
import com.example.webshopmenswear.model.Enum.OrderStatus;
import com.example.webshopmenswear.model.Enum.PaymentStatus;
import com.example.webshopmenswear.repository.*;
import com.example.webshopmenswear.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class OrderApi {
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final CartDetailRepository cartDetailRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PaymentRepository paymentRepository;


    @PostMapping("/orders")
    public ResponseEntity<String> createOrder(
            @RequestBody Map<String, Object> requestBody,
            HttpSession httpSession) {

        // Lấy thông tin từ request body
        String paymentMethod = (String) requestBody.get("paymentMethod");
        Integer addressId = (Integer) requestBody.get("addressId");
        Double totalPrice = (Double) requestBody.get("totalPrice");

        // Kiểm tra phương thức thanh toán
        if (paymentMethod == null || (!paymentMethod.equals("cash") && !paymentMethod.equals("bank"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payment method");
        }

        // Lấy người dùng từ session
        User currentUser = (User) httpSession.getAttribute("CURRENT_USER");
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        // Nếu phương thức thanh toán là "cash", tạo đơn hàng
        if (paymentMethod.equals("cash")) {

            // Tạo Order
            Order order = Order.builder()
                    .user(currentUser)
                    .orderStatus(OrderStatus.DANG_XU_LY)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            // Kiểm tra và gán địa chỉ nếu có
            if (addressId != null) {
                Optional<Address> addressOptional = addressRepository.findById(addressId);
                if (addressOptional.isPresent()) {
                    Address address = addressOptional.get();
                    order.setAddressUser(address); // Gắn địa chỉ vào đơn hàng
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Address not found");
                }
            }


            // Lưu đơn hàng
            orderRepository.save(order);

            // Lấy danh sách CartDetail từ giỏ hàng của người dùng
            List<CartDetail> cartDetails = cartDetailRepository.findByCart_UserId(currentUser.getId());
            if (cartDetails.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cart is empty");
            }

            // Khởi tạo tổng tiền đơn hàng
            double totalOrderPrice = 0.0;

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

                // Tính tổng tiền cho đơn hàng
                totalOrderPrice += orderDetail.getQuantity() * orderDetail.getPrice();

                // Lưu OrderDetail vào cơ sở dữ liệu
                orderDetailRepository.save(orderDetail);
            }

            // Cập nhật tổng tiền cho Order
            order.setTotalPrice(totalOrderPrice);
            orderRepository.save(order); // Lưu lại order sau khi cập nhật tổng tiền

            // Xóa các CartDetail sau khi chuyển sang OrderDetail (nếu cần)
            cartDetailRepository.deleteAll(cartDetails);

            // Xử lý phương thức thanh toán
            Payment payment = Payment.builder()
                    .order(order)
                    .paymentMethod("Cash on Delivery")
                    .paymentStatus(PaymentStatus.CHUA_THANH_TOAN)
                    .deliveryStatus("Awaiting Shipment")
                    .paymentDate(LocalDateTime.now())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            paymentRepository.save(payment);

            return ResponseEntity.ok("Order created successfully with Cash on Delivery.");
        }

        // Nếu phương thức thanh toán là "bank", chỉ lưu thông tin vào session mà không tạo đơn hàng
        else if (paymentMethod.equals("bank")) {
            // Tạo session để lưu thông tin đơn hàng và tổng tiền
            // Tạo session để lưu thông tin tổng tiền và phương thức thanh toán
            httpSession.setAttribute("totalPrice", totalPrice);  // Lưu tổng tiền vào session
            httpSession.setAttribute("paymentMethod", "bank");  // Lưu phương thức thanh toán vào session


            httpSession.setAttribute("addressId", addressId);  // Lưu addressId vào session
            System.out.println(addressId);
            // Điều hướng đến trang thanh toán qua ngân hàng (VNPay)
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("/home_VNpay"))  // Điều hướng đến trang VNPay
                    .build();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
    }


    @PutMapping("/{orderId}")
    public Order updateOrderTotalPrice(@PathVariable Integer orderId, @RequestBody Double newTotalPrice) {
        return orderService.updateOrderTotalPrice(orderId, newTotalPrice);
    }

    @PostMapping("/update-status")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(@RequestBody Map<String, Object> request) {
        Integer orderId = Integer.valueOf((String) request.get("orderId"));
        String status = (String) request.get("status");

        // Tìm đơn hàng theo ID
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (!orderOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "Order not found"));
        }

        // Lấy đối tượng đơn hàng
        Order order = orderOptional.get();

        // Cập nhật trạng thái đơn hàng
        if ("CANCEL".equals(status)) {
            order.setOrderStatus(OrderStatus.DA_HUY);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "Invalid status"));
        }

        // Lưu lại thay đổi trong cơ sở dữ liệu
        orderRepository.save(order);

        // Trả về kết quả thành công
        return ResponseEntity.ok(Map.of("success", true, "message", "Order status updated"));
    }
}
