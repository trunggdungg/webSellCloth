package com.example.webshopmenswear.rest;

import com.example.webshopmenswear.entity.OrderDetail;
import com.example.webshopmenswear.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orderdetails")
public class OrderDetailApi {
    private final OrderDetailService orderDetailService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDetail createOrderDetail(@RequestBody OrderDetail orderDetail) {
        return orderDetailService.createOrderDetail(orderDetail);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<List<OrderDetail>> getOrderDetailsByOrderId(@PathVariable Integer orderId) {
        List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(orderId);

        if (orderDetails.isEmpty()) {
            // Nếu không tìm thấy chi tiết đơn hàng, trả về mã trạng thái 404 Not Found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Nếu tìm thấy chi tiết đơn hàng, trả về danh sách cùng với mã trạng thái 200 OK
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }


}
