package com.example.webshopmenswear.rest;

import com.example.webshopmenswear.entity.OrderDetail;
import com.example.webshopmenswear.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

}
