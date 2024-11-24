package com.example.webshopmenswear.rest;

import com.example.webshopmenswear.entity.Product;
import com.example.webshopmenswear.entity.ProductVariant;
import com.example.webshopmenswear.service.ProductService;
import com.example.webshopmenswear.service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProductApi {
    @Autowired
    private final ProductService productService;
    private final ProductVariantService productVariantService;

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

    //    @GetMapping("/check-stock")
//    public ResponseEntity<?> checkStock(
//        @RequestParam Integer productId,
//        @RequestParam Integer colorId,
//        @RequestParam Integer sizeId) {
//        Optional<ProductVariant> variant = productVariantService.getVariantByProductAndColorAndSize(
//            productId, colorId, sizeId);
//
//        if (variant.isPresent()) {
//            Map<String, Object> response = new HashMap<>();
//            response.put("stock", variant.get().getStock());
//            response.put("available", variant.get().getStock() > 0);
//            return ResponseEntity.ok(response);
//        }
//
//        return ResponseEntity.notFound().build();
//    }
    @GetMapping("/filter")
    public ResponseEntity<Map<String, Object>> filterProducts(
        @RequestParam(required = false) Integer colorId,
        @RequestParam(required = false) Integer sizeId,
        @RequestParam(required = false) Integer categoryId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductVariant> result = productVariantService.filterProducts(colorId, sizeId, categoryId, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", result.getContent());  // List of ProductVariant objects
        response.put("totalPages", result.getTotalPages());  // Total number of pages
        response.put("totalElements", result.getTotalElements());  // Total number of elements

        // Trả về ResponseEntity với nội dung dưới dạng JSON
        return ResponseEntity.ok(response);
    }

}
