package com.example.webshopmenswear.rest;

import com.example.webshopmenswear.entity.Product;
import com.example.webshopmenswear.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductApi {
    @Autowired
    private final ProductService productService;

    @GetMapping("/load-more-products")
    public List<Product> LoadMoreProducts(@RequestParam Integer offset,
                                          @RequestParam Integer limit) {
        return productService.loadMoreProducts(offset, limit);
    }

    @GetMapping("/search-product/name/{name}")
    public Page<Product> listProductByName(@PathVariable String name) {
        // Lấy dữ liệu sản phẩm từ service
        Page<Product> productSearch = productService.findByNameContainingAndStatus(name, true, 1, 10);
        // Trả về dữ liệu dưới dạng JSON
        return productSearch;
    }
}
