package com.example.webshopmenswear.rest.admin;

import com.example.webshopmenswear.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/productImage")
public class ImageProductApiAdmin {

    private final ProductImageService productImageService;


}
