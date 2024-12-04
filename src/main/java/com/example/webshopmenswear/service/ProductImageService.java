package com.example.webshopmenswear.service;

import com.example.webshopmenswear.entity.ProductImage;
import com.example.webshopmenswear.repository.ProductImageRepository;
import com.example.webshopmenswear.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductImageService {
    private final ProductImageRepository productImageRepository;
    private final CloudinaryService cloudinaryService;
    private final ProductRepository productRepository;

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


    public String uploadImagesProduct(Integer id, MultipartFile file) {
        // Kiểm tra sản phẩm có tồn tại không
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }

        // Kiểm tra file có rỗng không
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        // Kiểm tra file có phải là ảnh không
        if (!file.getContentType().startsWith("image")) {
            throw new RuntimeException("File is not image");
        }

        // Kiểm tra số lượng ảnh đã upload
        int countImage = productImageRepository.findByProductId(id).size();
        if (countImage >= 10) {
            throw new RuntimeException("Product has 10 images");
        }

        try {
            // Upload ảnh lên cloudinary
            Map map = cloudinaryService.uploadFile(file, "productImg");
            System.out.println(map);
            String path = map.get("url").toString();

            // Lưu ảnh vào bảng product_images
            ProductImage productImage = ProductImage.builder()
                .product(productRepository.findById(id).get())
                .imageUrl(path)
                .isPrimary(false)
                .imageOrder(countImage + 1)
                .build();
            productImageRepository.save(productImage);

            return path;
        } catch (Exception e) {
            throw new RuntimeException("Upload file failed");
        }


    }
}
