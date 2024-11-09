package com.example.webshopmenswear;

import com.example.webshopmenswear.entity.*;
import com.example.webshopmenswear.model.Enum.OrderStatus;
import com.example.webshopmenswear.model.Enum.UserRole;
import com.example.webshopmenswear.repository.*;
import com.github.javafaker.Faker;
import com.github.slugify.Slugify;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    void loadColor() {
        List<Color> colors = colorRepository.findAll();
        System.out.println(colors);
    }

    @Test
    void findTop10() {
        Page<Product> products = productRepository.findTop10ByStatusOrderByCreatedAtDesc(true, PageRequest.of(0, 10));
        System.out.println(products.getContent());
    }

    @Test
    void saveCategories() {
        Faker faker = new Faker();
        Slugify slugify = Slugify.builder().build();

        for (int i = 0; i < 5; i++) {
            String name = faker.leagueOfLegends().champion();
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
    void saveDiscounts() {
        Faker faker = new Faker();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            double discountPercent = 10 + (20 * random.nextDouble()); // Generate a random value between 10 and 30
            Discount discount = Discount.builder()
                .name(faker.company().name())
                .description(faker.lorem().sentence())
                .discountPercent(discountPercent)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(faker.number().numberBetween(1, 30)))
                .build();
            discountRepository.save(discount);
        }
    }

    @Test
    void saveProducts() {
        Faker faker = new Faker();
        Slugify slugify = Slugify.builder().build();

        Random rd = new Random();

        List<Category> categories = categoryRepository.findAll();
        List<Discount> discounts = discountRepository.findAll();

        for (int i = 0; i < 20; i++) {
            List<Discount> randDiscounts = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                Discount randDiscount = discounts.get(rd.nextInt(discounts.size()));
                if (!randDiscounts.contains(randDiscount)) {
                    randDiscounts.add(randDiscount);
                }
            }

            Product product = Product.builder()
                .name(faker.food().ingredient())
                .slug(slugify.slugify(faker.food().ingredient()))
                .description(faker.lorem().sentence())
                .price(faker.number().randomDouble(2, 10, 100))
                .category(categories.get(rd.nextInt(categories.size())))
                .status(true)
                .discounts(randDiscounts)
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
            // Chọn ngẫu nhiên từ 2 đến 4 màu từ danh sách colors
            Collections.shuffle(colors);
            List<Color> selectedColors = colors.subList(0, rd.nextInt(3) + 2);

            // Chọn ngẫu nhiên từ 2 đến 4 kích thước từ danh sách sizes
            Collections.shuffle(sizes);
            List<Size> selectedSizes = sizes.subList(0, rd.nextInt(3) + 2);

            for (Color color : selectedColors) {
                for (Size size : selectedSizes) {
                    // Kiểm tra nếu biến thể đã tồn tại
                    if (!productVariantRepository.existsByProductIdAndColorIdAndSizeId(product.getId(), color.getId(), size.getId())) {
                        ProductVariant variant = ProductVariant.builder()
                            .product(product)
                            .color(color)
                            .size(size)
                            .stock(rd.nextInt(50) + 1) // Số lượng tồn kho ngẫu nhiên từ 1-50
                            .build();
                        productVariantRepository.save(variant);
                    }
                }
            }
        }
    }

    @Test
    void saveProvinces() {
        Faker faker = new Faker();
        for (int i = 0; i < 10; i++) {
            Province province = Province.builder()
                .provinceName(faker.address().state())
                .build();
            provinceRepository.save(province);
        }
    }

    @Test
    void saveDistricts() {
        Faker faker = new Faker();
        Random random = new Random();
        List<Province> provinces = provinceRepository.findAll();
        for (Province province : provinces) {
            int numberOfDistricts = random.nextInt(10) + 1;
            for (int i = 0; i < numberOfDistricts; i++) {
                District district = District.builder()
                    .districtName(faker.address().city())
                    .province(province)
                    .build();
                districtRepository.save(district);
            }
        }
    }

    @Test
    void saveWards() {
        Faker faker = new Faker();
        Random random = new Random();
        List<District> districts = districtRepository.findAll();
        for (District district : districts) {
            int numberOfWards = random.nextInt(10) + 1;
            for (int i = 0; i < numberOfWards; i++) {
                Ward ward = Ward.builder()
                    .wardName(faker.address().cityName())
                    .district(district)
                    .build();
                wardRepository.save(ward);
            }
        }
    }

    @Test
    void saveUsers() {
        Faker faker = new Faker();
        for (int i = 0; i < 10; i++) {
            User user = User.builder()
                .username(faker.name().username())
                .password(faker.internet().password())
                .fullName(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().cellPhone())
                .userRole(i == 0 || i == 1 ? UserRole.ADMIN : UserRole.USER)
                .avatar("https://placehold.co/150x150?text=" + faker.name().fullName().substring(0, 1).toUpperCase())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
            userRepository.save(user);
        }
    }

    @Test
    void saveAddresses() {
        Faker faker = new Faker();
        List<User> users = userRepository.findAll();
        List<Province> provinces = provinceRepository.findAll();
        List<District> districts = districtRepository.findAll();
        List<Ward> wards = wardRepository.findAll();
        Random random = new Random();

        for (User user : users) {
            Address address = Address.builder()
                .user(user)
                .street(faker.address().streetAddress())
                .province(provinces.get(random.nextInt(provinces.size())))
                .district(districts.get(random.nextInt(districts.size())))
                .ward(wards.get(random.nextInt(wards.size())))
                .createdAt(LocalDateTime.now())
                .build();
            addressRepository.save(address);
        }
    }

    @Test
    void saveOrders() {
        List<User> users = userRepository.findAll();
        List<Address> addresses = addressRepository.findAll();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            User user = users.get(random.nextInt(users.size()));
            Address address = addresses.get(random.nextInt(addresses.size()));

            // Lấy số ngẫu nhiên từ 0 đến 2 cho trạng thái đơn hàng
            int orderStatusIndex = random.nextInt(3);
            OrderStatus orderStatus = OrderStatus.values()[orderStatusIndex];

            // Tạo và lưu đơn hàng
            Order order = Order.builder()
                .user(user)
                .addressUser(address) // Thêm địa chỉ vào đơn hàng
                .orderStatus(orderStatus) // Gán trạng thái cho đơn hàng
                .totalPrice(0.0) // Khởi tạo giá trị đơn hàng
                .createdAt(LocalDateTime.now())
                .build();

            orderRepository.save(order);
        }
    }


}
