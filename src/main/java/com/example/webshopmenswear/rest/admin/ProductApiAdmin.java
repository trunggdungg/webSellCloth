package com.example.webshopmenswear.rest.admin;

import com.example.webshopmenswear.entity.Product;
import com.example.webshopmenswear.model.request.UpSertProductRequest;
import com.example.webshopmenswear.model.response.ErrorResponse;
import com.example.webshopmenswear.model.response.FileResponse;
import com.example.webshopmenswear.service.ProductImageService;
import com.example.webshopmenswear.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/products")
public class ProductApiAdmin {
    private final ProductService productService;
    private final ProductImageService productImageService;

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

    @PostMapping("/{id}/images")
    public ResponseEntity<?> uploadImage(@PathVariable Integer id, @RequestParam MultipartFile file) {
        String path = productService.uploadImage(id, file);
        FileResponse fileResponse = FileResponse.builder()
            .url(path)
            .build();
        return ResponseEntity.ok(fileResponse);
    }

    @PostMapping("/{id}/imagesProduct")
    public ResponseEntity<?> uploadImagesProduct(@PathVariable Integer id, @RequestParam MultipartFile file) {
        try {
            // Xử lý upload ảnh và lưu vào bảng product_images
            String path = productImageService.uploadImagesProduct(id, file);

            // Tạo đối tượng FileResponse
            FileResponse fileResponseImg = FileResponse.builder()
                .url(path)
                .build();

            // Trả về kết quả thành công
            return ResponseEntity.ok(fileResponseImg);
        } catch (Exception e) {
            // Xử lý lỗi khi upload
            ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


}
