package com.example.webshopmenswear.service;

import com.example.webshopmenswear.entity.ProductImage;
import com.example.webshopmenswear.repository.IProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageService {
    private final IProductImageRepository productImageRepository;

    public List<ProductImage> getListImagesProduct(Integer id) {
        return productImageRepository.findByProductId(id);
    }
}
