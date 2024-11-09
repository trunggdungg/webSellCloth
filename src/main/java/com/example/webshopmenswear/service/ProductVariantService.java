package com.example.webshopmenswear.service;

import com.example.webshopmenswear.entity.Color;
import com.example.webshopmenswear.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductVariantService {
    private final ProductVariantRepository productVariantRepository;

    public List<Color> getProductColors(Integer productId) {
        return productVariantRepository.findDistinctColorsByProductId(productId);
    }

    public List<Color> getSize(Integer productId) {
        return productVariantRepository.findDistinctColorsByProductId(productId);
    }
}
