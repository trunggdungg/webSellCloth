package com.example.webshopmenswear.rest;

import com.example.webshopmenswear.entity.Product;
import com.example.webshopmenswear.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductApi {
    @Autowired
    private final ProductService productService;

    @GetMapping("/load-more-products")
    public List<Product> LoadMoreProducts(@RequestParam Integer offset,
                                          @RequestParam Integer limit) {
        return productService.loadMoreProducts(offset, limit);
    }
}
