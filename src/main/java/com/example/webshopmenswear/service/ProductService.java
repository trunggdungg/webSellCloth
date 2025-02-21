package com.example.webshopmenswear.service;

import com.example.webshopmenswear.entity.Category;
import com.example.webshopmenswear.entity.Product;
import com.example.webshopmenswear.entity.ProductImage;
import com.example.webshopmenswear.model.request.UpSertProductRequest;
import com.example.webshopmenswear.repository.ProductImageRepository;
import com.example.webshopmenswear.repository.ProductRepository;
import com.github.slugify.Slugify;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProductImageRepository productImageRepository;
    private final CloudinaryService cloudinaryService;


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

    public List<Product> getTop4Product(Integer productId, String slug) {
        Product selectproduct = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("Khong thay"));
        Double targetprice = selectproduct.getPrice();
        Pageable pageable = PageRequest.of(0, 4);
        return productRepository.findTop4ByClosestPrice(targetprice, productId, pageable);

    }


    public List<Product> findAllByStatusOrderByCreatedAtDesc(boolean b) {
        return productRepository.findAll(Sort.by("CreatedAt").descending());
    }

    public List<Product> findTop4ByCategoryAndStatus(Integer categoryId, Integer productId) {
        return productRepository.findTop4ByCategoryAndStatus(categoryId, productId);
    }


    //product admin
    public List<Product> getAllProduct() {
        return productRepository.findAll(Sort.by("CreatedAt").descending());
    }

    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Không tìm thấy sản phẩm"));
    }

    public Product upSertProduct(Integer id, UpSertProductRequest request) {
        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Không tìm thấy sản phẩm"));
        System.out.println("product: " + product);
        Category category = categoryService.findById(request.getCategoryId());

        Slugify slg = Slugify.builder().build();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDiscount(request.getDiscount());
        product.setMaterial(request.getMaterial());
        product.setSlug(slg.slugify(request.getName()));
        product.setDescription(request.getDescription());
        product.setStatus(request.getStatus());
        product.setCategory(category);
        product.setUpdatedAt(LocalDateTime.now());

        return productRepository.save(product);
    }

    public Product createProduct(UpSertProductRequest request) {
        Category category = categoryService.findById(request.getCategoryId());
        Slugify slg = Slugify.builder().build();

        Product product = Product.builder()
            .name(request.getName())
            .price(request.getPrice())
            .material(request.getMaterial())
            .discount(request.getDiscount())
            .description(request.getDescription())
            .slug(slg.slugify(request.getName()))
            .status(request.getStatus())
            .category(category)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        return productRepository.save(product);
    }

    public String uploadImage(Integer id, MultipartFile file) {
        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Không tìm thấy sản phẩm"));
        try {
            Map map = cloudinaryService.uploadFile(file, "productImg");
            System.out.println(map);
            String path = map.get("url").toString();
            product.setImageUrlPrimary(path);
            productRepository.save(product);
            return path;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi upload ảnh");
        }
    }


    public Object getAll() {
        return productRepository.findAll();
    }
}
