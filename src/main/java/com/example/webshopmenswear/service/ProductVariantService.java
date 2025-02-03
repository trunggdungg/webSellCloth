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

    public List<ProductVariant> getAllProductVariant() {
        return productVariantRepository.findAll();
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

        // Trường hợp categoryId là 0, chỉ lọc theo colorId và sizeId
        if (categoryId == 0) {
            // Sử dụng các hàm tìm kiếm liên quan đến màu sắc và kích thước
            if (colorId != 0 && sizeId != 0) {
                return findByColorAndSize(colorId, sizeId, pageable);
            } else if (colorId != 0) {
                return findByColor(colorId, pageable);
            } else if (sizeId != 0) {
                return findBySize(sizeId, pageable);
            }
            return productVariantRepository.findAll(pageable);  // Trả về tất cả sản phẩm nếu không có màu sắc hoặc kích thước
        }

        // Trường hợp có cả colorId, sizeId và categoryId
        if (colorId != 0 && sizeId != 0 && categoryId != 0) {
            return productVariantRepository.findByColorAndSizeAndCategory(colorId, sizeId, categoryId, pageable);
        }

        // Trường hợp chỉ có colorId và sizeId
        if (colorId != 0 && sizeId != 0) {
            return findByColorAndSize(colorId, sizeId, pageable);
        }

        // Trường hợp chỉ có colorId
        if (colorId != 0) {
            return findByColor(colorId, pageable);
        }

        // Trường hợp chỉ có sizeId
        if (sizeId != 0) {
            return findBySize(sizeId, pageable);
        }

        // Trường hợp chỉ có categoryId (và categoryId không phải là 0)
        if (categoryId != 0 && categoryId != 0) {
            return findByCategory(categoryId, pageable);
        }

        // Trường hợp có colorId và categoryId, nhưng không có sizeId
        if (colorId != 0 && categoryId != 0) {
            return findByColorAndCategory(colorId, categoryId, pageable);
        }

        // Trường hợp có sizeId và categoryId, nhưng không có colorId
        if (sizeId != 0 && categoryId != 0) {
            return findBySizeAndCategory(sizeId, categoryId, pageable);
        }

        // Nếu không có điều kiện lọc nào, trả về tất cả sản phẩm
        return productVariantRepository.findAll(pageable);
    }


    // Phương thức tìm sản phẩm theo màu sắc và kích thước
    public Page<ProductVariant> findByColorAndSize(Integer colorId, Integer sizeId, Pageable pageable) {
        return productVariantRepository.findByColorAndSize(colorId, sizeId, pageable);
    }

    // Tìm sản phẩm theo màu sắc
    public Page<ProductVariant> findByColor(Integer colorId, Pageable pageable) {
        return productVariantRepository.findByColor(colorId, pageable);
    }

    // Tìm sản phẩm theo kích thước
    public Page<ProductVariant> findBySize(Integer sizeId, Pageable pageable) {
        return productVariantRepository.findBySize(sizeId, pageable);
    }

    // Tìm sản phẩm theo loại sản phẩm
    public Page<ProductVariant> findByCategory(Integer categoryId, Pageable pageable) {
        return productVariantRepository.findByCategory(categoryId, pageable);
    }

    // Tìm sản phẩm theo màu sắc và loại sản phẩm
    public Page<ProductVariant> findByColorAndCategory(Integer colorId, Integer categoryId, Pageable pageable) {
        return productVariantRepository.findByColorAndCategory(colorId, categoryId, pageable);
    }

    // Tìm sản phẩm theo kích thước và loại sản phẩm
    public Page<ProductVariant> findBySizeAndCategory(Integer sizeId, Integer categoryId, Pageable pageable) {
        return productVariantRepository.findBySizeAndCategory(sizeId, categoryId, pageable);
    }


}
