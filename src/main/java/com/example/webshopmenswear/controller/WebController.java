package com.example.webshopmenswear.controller;

import com.example.webshopmenswear.entity.*;
import com.example.webshopmenswear.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {
    private final ProductService productService;
    private final ProductImageService productImageService;
    private final ProductVariantService productVariantService;
    private final ColorService colorService;
    private final SizeService sizeService;
    private final CategoryService categoryService;

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

        List<Color> colors = colorService.getAllColors();
        List<Size> sizes = sizeService.getAllSize();
        List<Category> categories = categoryService.getAllCategory();

        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("colors", colors);
        model.addAttribute("sizes", sizes);
        model.addAttribute("categories", categories);
        return "/web/shop";
    }

    @GetMapping("/")
    public String HomePage(@RequestParam(required = false, defaultValue = "1") int page,
                           @RequestParam(required = false, defaultValue = "10") int pageSize,
                           Model model) {
        Page<Product> products = productService.findTop10ByStatusOrderByCreatedAtDesc(true, page, pageSize);

        model.addAttribute("productsPage", products);
        model.addAttribute("currentPage", page);


        return "/web/index";
    }

    @GetMapping("/product/{id}/{slug}")
    public String ProductDetail(@PathVariable Integer id, @PathVariable String slug, Model model) {
        Product product = productService.getProductDetail(id, slug);
        List<ProductImage> productImages = productService.getImageByProductId(id);
        List<Product> get4product = productService.getTop4Product(id, slug);

        List<Product> productCategory = productService.findTop4ByCategoryAndStatus(product.getCategory().getId(), id);

        List<Color> colors = productVariantService.getProductColors(id);
        List<Size> sizes = productVariantService.getProductSizes(id);
        List<ProductVariant> variants = productVariantService.getProductVariants(id);

        model.addAttribute("productsDetail", product);
        model.addAttribute("productImages", productImages);
        model.addAttribute("get4product", get4product);
        model.addAttribute("productCategory", productCategory);
        model.addAttribute("colors", colors);
        model.addAttribute("sizes", sizes);
        model.addAttribute("variants", variants);

        return "/web/product";
    }


    @GetMapping("/account")
    public String LoginPage(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("CURRENT_USER");
        if (user != null) {
            return "redirect:/";
        }
        return "/web/account";
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

    @GetMapping("/about")
    public String AboutPage() {
        return "/web/about";
    }

    @GetMapping("/checkout")
    public String CheckoutPage() {
        return "/web/checkout";
    }


    @GetMapping("/404")
    public String Page404() {
        return "/web/404";
    }

    @GetMapping("/contact")
    public String ContactPage() {
        return "/web/contact";
    }
}
