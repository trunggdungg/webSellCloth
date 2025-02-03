package com.example.webshopmenswear.config.VNPay;

import com.example.webshopmenswear.entity.*;
import com.example.webshopmenswear.model.Enum.OrderStatus;
import com.example.webshopmenswear.model.Enum.PaymentStatus;
import com.example.webshopmenswear.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class VNPAYController {
    @Autowired
    private VNPAYService vnPayService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CartDetailRepository cartDetailRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @GetMapping("/home_VNpay")
    public String home(HttpSession httpSession, Model model) {
        // Lấy thông tin từ session
        Double totalPrice = (Double) httpSession.getAttribute("totalPrice");
        Integer orderId = (Integer) httpSession.getAttribute("orderId");
        // Kiểm tra xem có thông tin cần thiết không
        if (totalPrice == null) {
            return "redirect:/error";  // Redirect về trang lỗi nếu thiếu thông tin
        }
        int totalPriceInt = totalPrice.intValue() * 1000;
        // Truyền giá trị session vào model để Thymeleaf có thể hiển thị
        model.addAttribute("totalPriceInt", totalPriceInt);
        model.addAttribute("orderId", orderId);
        return "web/createOrder";  // Trả về trang xử lý thanh toán
    }

    // Chuyển hướng người dùng đến cổng thanh toán VNPAY
    @PostMapping("/submitOrder")
    public String submidOrder(@RequestParam("amount") int orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(request, orderTotal, orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }

    // Sau khi hoàn tất thanh toán, VNPAY sẽ chuyển hướng trình duyệt về URL này
    @GetMapping("/vnpay-payment-return")
    public String paymentCompleted(HttpServletRequest request, Model model, HttpSession httpSession) {
        int paymentStatus = vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        User currentUser = (User) httpSession.getAttribute("CURRENT_USER");
        Double price = (Double) httpSession.getAttribute("totalPrice");
        Integer addressId = (Integer) httpSession.getAttribute("addressId");
        System.out.println(addressId);
        if (paymentStatus == 1) {
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

                    // Lưu địa chỉ vào đơn hàng nếu cần thêm thông tin hiển thị
                    System.out.println("Địa chỉ: " + address.getId());
                } else {
                    throw new RuntimeException("Address not found for ID: " + addressId);
                }
            }

            // Lưu đơn hàng
            orderRepository.save(order);

            // Lấy danh sách CartDetail từ giỏ hàng của người dùng
            List<CartDetail> cartDetails = cartDetailRepository.findByCart_UserId(currentUser.getId());

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


                // Lưu OrderDetail vào cơ sở dữ liệu
                orderDetailRepository.save(orderDetail);
            }
            // Cập nhật tổng tiền cho Order
            order.setTotalPrice(price);
            orderRepository.save(order); // Lưu lại order sau khi cập nhật tổng tiền

            // Xóa các CartDetail sau khi chuyển sang OrderDetail (nếu cần)
            cartDetailRepository.deleteAll(cartDetails);

            // Xử lý phương thức thanh toán
            Payment payment = Payment.builder()
                    .order(order)
                    .paymentMethod("Bank Transfer")
                    .paymentStatus(PaymentStatus.DA_THANH_TOAN)
                    .deliveryStatus("Awaiting Shipment")
                    .paymentDate(LocalDateTime.now())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            paymentRepository.save(payment);
        }
        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        return paymentStatus == 1 ? "/web/orderSuccess" : "/web/orderFail";
    }
}
