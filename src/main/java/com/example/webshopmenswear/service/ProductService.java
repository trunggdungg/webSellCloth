package com.example.webshopmenswear.service;

import com.example.webshopmenswear.entity.Product;
import com.example.webshopmenswear.entity.ProductImage;
import com.example.webshopmenswear.repository.ProductImageRepository;
import com.example.webshopmenswear.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    public List<Product> loadMoreProducts(Integer offset, Integer limit) {
        return productRepository.findAll(PageRequest.of(offset, limit)).getContent();
    }

    public Page<Product> getProductsByStatus(Boolean status, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("UpdatedAt").descending());
        return productRepository.findByStatus(status, pageable);
    }

    public Page<Product> findByNameContainingAndStatus(String name, Boolean status, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("UpdatedAt").descending());
        return productRepository.findByNameContainingAndStatus(name, status, pageable);
    }

    public Page<Product> findTop10ByStatusOrderByCreatedAtDesc(Boolean status, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("CreatedAt").descending());
        return productRepository.findTop10ByStatusOrderByCreatedAtDesc(status, pageable);
    }

    public ProductImage findFirtProductImage(Integer productId) {
        return productImageRepository.findByProductAndIsPrimary(productRepository.findById(productId).get(), true);
    }
}
