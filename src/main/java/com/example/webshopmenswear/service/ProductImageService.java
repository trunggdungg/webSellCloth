package com.example.webshopmenswear.service;

import com.example.webshopmenswear.entity.ProductImage;
import com.example.webshopmenswear.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageService {
    private final ProductImageRepository productImageRepository;

    public List<ProductImage> getListImagesProduct(Integer id) {
        return productImageRepository.findByProductId(id);
    }

    public ProductImage findFirstByProductId(Integer productId) {
        // Ưu tiên lấy ảnh primary trước
        ProductImage primaryImage = productImageRepository.findFirstByProductIdAndIsPrimaryTrue(productId);
        if (primaryImage != null) {
            return primaryImage;
        }
        // Nếu không có ảnh primary thì lấy ảnh đầu tiên theo thứ tự
        return productImageRepository.findFirstByProductIdOrderByImageOrder(productId);
    }

    public List<ProductImage> findTop2ImagesByProductId(Integer productId) {
        return productImageRepository.findTop2ByProductIdOrderByImageOrder(productId);
    }


    public List<ProductImage> findByProductId(Integer id) {
        return productImageRepository.findByProductId(id);
    }
}
