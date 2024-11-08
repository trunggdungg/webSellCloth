package com.example.webshopmenswear;

import com.example.webshopmenswear.entity.*;
import com.example.webshopmenswear.repository.*;
import com.github.javafaker.Faker;
import com.github.slugify.Slugify;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@SpringBootTest
class WebShopMenswearApplicationTests {
    @Autowired
    private ICategoryRepository categoryRepository;

    @Autowired
    private IColorRepository colorRepository;

    @Autowired
    private ISizeRepository sizeRepository;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IProductImageRepository imageRepository;

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
                    .parent(null)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            categoryRepository.save(category);
        }
    }

    @Test
    void saveSubCategories() {
        List<Category> categories = categoryRepository.findAll();
        Faker faker = new Faker();
        Slugify slugify = Slugify.builder().build();
        Random rd = new Random();

        for (int i = 0; i < 10; i++) {
            String name = faker.leagueOfLegends().champion();
            String slug = slugify.slugify(name);

            Category category = Category.builder()
                    .name(name)
                    .slug(slug)
                    .parent(categories.get(rd.nextInt(categories.size())))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            categoryRepository.save(category);
        }

    }

    @Test
    void saveColors() {
        Faker faker = new Faker();

        for (int i = 0; i < 20; i++) {
            Color color = Color.builder()
                    .nameColor(faker.color().name())
                    .build();

            colorRepository.save(color);
        }

    }

    @Test
    void saveSizes() {
        Size size = Size.builder()
                .sizeName("XL")
                .build();

        sizeRepository.save(size);
    }

    @Test
    void saveProducts() {

        List<Category> categories = categoryRepository.findAll();

        Faker faker = new Faker();
        Slugify slugify = Slugify.builder().build();
        Random rd = new Random();
        Boolean status = faker.bool().bool();

        for (int i = 0; i < 50; i++) {
            String name = faker.pokemon().name();

            Product product = Product.builder()
                    .name(name)
                    .slug(slugify.slugify(name))
                    .category(categories.get(rd.nextInt(categories.size())))
                    .description(faker.lorem().paragraph())
                    .price(faker.number().randomDouble(0, 100, 1000))
                    .status(status)
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
            ProductImage image = ProductImage.builder()
                    .product(product)
                    .imageUrl("https://placehold.co/600x400?text=" + product.getName().substring(0, 1).toUpperCase())
                    .altText(slugify.slugify(product.getName()))
                    .isPrimary(true)
                    .imageOrder(0)
                    .build();
            imageRepository.save(image);
        }
    }

}
