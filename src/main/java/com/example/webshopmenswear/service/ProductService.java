package com.example.webshopmenswear.service;

import com.example.webshopmenswear.entity.Product;
import com.example.webshopmenswear.entity.ProductImage;
import com.example.webshopmenswear.repository.ProductImageRepository;
import com.example.webshopmenswear.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
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

    public Product getProductDetail(Integer id, String slug) {
        return productRepository.findByIdAndSlugAndStatus(id, slug, true);
    }

    public List<ProductImage> getImageByProductId(Integer id) {
        return productImageRepository.findAllByProduct_Id(id);
    }

    public List<Product> getTop3Product(Integer productId, String slug) {
        Product selectproduct = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("Khong thay"));
        Double targetprice = selectproduct.getPrice();
        Pageable pageable = PageRequest.of(0, 4);
        return productRepository.findTop4ByClosestPrice(targetprice, productId, pageable);

    }


    public List<Product> findAllByStatusOrderByCreatedAtDesc(boolean b) {
        return productRepository.findAll(Sort.by("CreatedAt").descending());
    }
}
