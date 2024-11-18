package com.example.webshopmenswear.rest.admin;

import com.example.webshopmenswear.entity.Product;
import com.example.webshopmenswear.model.request.UpSertProductRequest;
import com.example.webshopmenswear.model.response.ErrorResponse;
import com.example.webshopmenswear.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/products")
public class ProductApiAdmin {
    private final ProductService productService;

    @PutMapping("/{id}")
    public ResponseEntity<?> upSertProduct(@PathVariable Integer id, @RequestBody UpSertProductRequest request) {
        Product product = productService.upSertProduct(id, request);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody UpSertProductRequest request) {
        try {
            // them moi san pham
            Product product = productService.createProduct(request);
            return ResponseEntity.ok(product);

        } catch (Exception e) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


}
