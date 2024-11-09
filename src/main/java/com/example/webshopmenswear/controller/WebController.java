package com.example.webshopmenswear.controller;

import com.example.webshopmenswear.entity.Product;
import com.example.webshopmenswear.entity.ProductImage;
import com.example.webshopmenswear.service.ProductImageService;
import com.example.webshopmenswear.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class WebController {
    private final ProductService productService;
    private final ProductImageService productImageService;


    @GetMapping("/shop")
    public String ProductsPage(@RequestParam(required = false, defaultValue = "1") int page,
                               @RequestParam(required = false, defaultValue = "10") int pageSize,
                               @RequestParam(required = false) String name,
                               Model model) {
        Page<Product> products = null;
        if (StringUtils.hasText(name)) {
            products = productService.findByNameContainingAndStatus(name, true, page, pageSize);
        } else {
            products = productService.getProductsByStatus(true, page, pageSize);
        }
        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        return "/web/shop";
    }

    @GetMapping("/")
    public String HomePage(@RequestParam(required = false, defaultValue = "1") int page,
                           @RequestParam(required = false, defaultValue = "10") int pageSize,
                           Model model) {
        Page<Product> products = productService.findTop10ByStatusOrderByCreatedAtDesc(true, page, pageSize);
        // Lấy danh sách ảnh cho từng sản phẩm
        //  Lưu ảnh đầu tiên của mỗi sản phẩm
        Map<Integer, ProductImage> productFirstImageMap = new HashMap<>();

        // Lấy ảnh đầu tiên của mỗi sản phẩm
        for (Product product : products.getContent()) {
            ProductImage firstImage = productImageService.findFirstByProductId(product.getId());
            if (firstImage != null) {
                productFirstImageMap.put(product.getId(), firstImage);
            }
        }

        model.addAttribute("productsPage", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("productFirstImageMap", productFirstImageMap);

        return "/web/index";
    }

    @GetMapping("/product/{id}/{slug}")
    public String ProductDetail(@PathVariable Integer id, @PathVariable String slug, Model model) {
        Product product = productService.getProductDetail(id, slug);
        model.addAttribute("ProductsDetail", product);
        return "/web/product";
    }

    @GetMapping("/product")
    public String ProductDetail() {

        return "/web/product";
    }

    @GetMapping("/blog")
    public String BlogPage() {
        return "/web/blog";
    }

    @GetMapping("/wishlist")
    public String WishListPage() {
        return "/web/wishlist";
    }

    @GetMapping("/cart")
    public String CartPage() {
        return "/web/cart";
    }

    @GetMapping("/checkout")
    public String CheckoutPage() {
        return "/web/checkout";
    }

    @GetMapping("/account")
    public String AccountPage() {
        return "/web/account";
    }

}
