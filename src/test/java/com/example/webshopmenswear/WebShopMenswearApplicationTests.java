package com.example.webshopmenswear;

import com.example.webshopmenswear.entity.*;
import com.example.webshopmenswear.repository.*;
import com.github.javafaker.Faker;
import com.github.slugify.Slugify;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@SpringBootTest
class WebShopMenswearApplicationTests {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository imageRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartDetailRepository cartDetailRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private FeedBackRepository feedBackRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private WardRepository wardRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    void saveCategories() {
        Faker faker = new Faker();
        Slugify slugify = Slugify.builder().build();

        for (int i = 0; i < 5; i++) {
            String name = faker.leagueOfLegends().rank();
            String slug = slugify.slugify(name);
            Category category = Category.builder()
                .name(name)
                .slug(slug)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
            categoryRepository.save(category);
        }
    }

    @Test
    void saveColors() {
        Faker faker = new Faker();
        for (int i = 0; i < 5; i++) {
            Color color = Color.builder()
                .nameColor(faker.color().name())
                .build();
            colorRepository.save(color);
        }
    }

    @Test
    void saveSizes() {
        List<String> sizes = List.of("S", "M", "L", "XL", "XXL");
        for (String sizeName : sizes) {
            Size size = Size.builder()
                .sizeName(sizeName)
                .build();
            sizeRepository.save(size);
        }
    }

    @Test
    void saveProducts() {
        Faker faker = new Faker();
        Slugify slugify = Slugify.builder().build();

        Random rd = new Random();

        List<Category> categories = categoryRepository.findAll();
        for (int i = 0; i < 50; i++) {
            String name = faker.leagueOfLegends().champion();
            Product product = Product.builder()
                .name(name)
                .slug(slugify.slugify(name))
                .category(categories.get(rd.nextInt(categories.size())))
                .description(faker.lorem().paragraph())
                .price(faker.number().randomDouble(0, 13000, 41000))
                .status(rd.nextBoolean())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

            productRepository.save(product);
        }
    }

    @Test
    void saveImagesProduct() {
        Slugify slugify = Slugify.builder().build();
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            List<ProductImage> existingImages = imageRepository.findByProduct(product);

            // Nếu sản phẩm có ít hơn 3 ảnh, thêm vào đủ 3 ảnh
            if (existingImages.size() < 3) {
                for (int i = 0; i < 3 - existingImages.size(); i++) {
                    ProductImage image = ProductImage.builder()
                        .product(product)
                        .imageUrl("https://placehold.co/600x400?text=" + product.getName().substring(0, 1).toUpperCase())
                        .altText(slugify.slugify(product.getName()) + "-" + (i + existingImages.size() + 1))
                        .isPrimary(existingImages.isEmpty() && i == 0) // Đặt ảnh đầu tiên là ảnh chính nếu chưa có ảnh chính
                        .imageOrder(i + 1 + existingImages.size())
                        .build();

                    imageRepository.save(image);
                }
            }
        }
    }


    @Test
    void saveProductVariants() {
        List<Product> products = productRepository.findAll();
        List<Color> colors = colorRepository.findAll();
        List<Size> sizes = sizeRepository.findAll();
        Random rd = new Random();

        for (Product product : products) {
            // Chọn ngẫu nhiên 2-4 màu từ danh sách colors
            Collections.shuffle(colors);
            List<Color> selectedColors = colors.subList(0, rd.nextInt(3) + 2); // Lấy từ 2 đến 4 màu

            // Chọn ngẫu nhiên 2-4 kích thước từ danh sách sizes
            Collections.shuffle(sizes);
            List<Size> selectedSizes = sizes.subList(0, rd.nextInt(3) + 2); // Lấy từ 2 đến 4 kích thước

            // Tạo các biến thể dựa trên màu và kích thước đã chọn
            for (Color color : selectedColors) {
                for (Size size : selectedSizes) {
                    ProductVariant variant = ProductVariant.builder()
                        .product(product)
                        .color(color)
                        .size(size)
                        .stock(rd.nextInt(20) + 1) // Số lượng ngẫu nhiên từ 1-50
                        .build();
                    productVariantRepository.save(variant);
                }
            }
        }
    }

}
