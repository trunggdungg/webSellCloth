package com.example.webshopmenswear.service;

import com.example.webshopmenswear.entity.Product;
import com.example.webshopmenswear.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final IProductRepository productRepository;

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
}
