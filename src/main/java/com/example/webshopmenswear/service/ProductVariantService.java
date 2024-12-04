package com.example.webshopmenswear.service;

import com.example.webshopmenswear.entity.Color;
import com.example.webshopmenswear.entity.ProductVariant;
import com.example.webshopmenswear.entity.Size;
import com.example.webshopmenswear.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductVariantService {
    private final ProductVariantRepository productVariantRepository;

    public List<Color> getProductColors(Integer productId) {
        return productVariantRepository.findDistinctColorsByProductId(productId);
    }

    public List<Size> getProductSizes(Integer productId) {
        List<Size> sizes = productVariantRepository.findDistinctSizesByProductId(productId, Sort.unsorted());
        return sizes.stream()
            .sorted(Comparator.comparing(Size::getId))
            .collect(Collectors.toList());
    }

    public List<ProductVariant> getProductVariants(Integer productId) {
        return productVariantRepository.findByProductId(productId);
    }

    public Optional<ProductVariant> getVariantByProductAndColorAndSize(
        Integer productId, Integer colorId, Integer sizeId) {
        return productVariantRepository.findByProductIdAndColorIdAndSizeId(productId, colorId, sizeId);
    }

    public ProductVariant getProductVariantById(Integer productId) {
        return productVariantRepository.findById(productId).orElse(null);
    }

    public Page<ProductVariant> filterProducts(
        Integer colorId,
        Integer sizeId,
        Integer categoryId,
        Pageable pageable) {
        return productVariantRepository.findByColorAndSizeAndCategory(colorId, sizeId, categoryId, pageable);
    }

    public List<ProductVariant> getAllProductVariant() {
        return productVariantRepository.findAll();
    }
}
