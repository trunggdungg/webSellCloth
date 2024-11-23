package com.example.webshopmenswear.controller;

import com.example.webshopmenswear.entity.*;
import com.example.webshopmenswear.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class WebController {
    private final ProductService productService;
    private final ProductImageService productImageService;
    private final ProductVariantService productVariantService;
    private final ColorService colorService;
    private final SizeService sizeService;
    private final CategoryService categoryService;
    private final ProvinceService provinceService;
    private final DistrictService districtService;
    private final wardService wardService;

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

        Map<Integer, ProductImage> productFirstImageMap = new HashMap<>();

        // Lấy ảnh đầu tiên của mỗi sản phẩm
        for (Product product : products.getContent()) {
            ProductImage firstImage = productImageService.findFirstByProductId(product.getId());
            if (firstImage != null) {
                productFirstImageMap.put(product.getId(), firstImage);
            }
        }

        List<Color> colors = colorService.getAllColors();
        List<Size> sizes = sizeService.getAllSize();
        List<Category> categories = categoryService.getAllCategory();

        model.addAttribute("productFirstImageMap", productFirstImageMap);
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
        List<Product> productsList = productService.findAllByStatusOrderByCreatedAtDesc(true);
        // Lấy danh sách ảnh cho từng sản phẩm
        //  Lưu ảnh đầu tiên của mỗi sản phẩm
        Map<Integer, ProductImage> productFirstImageMap = new HashMap<>();
        //luu tat ca anh
        Map<Integer, List<ProductImage>> productImagesMap = new HashMap<>();

        // Lấy ảnh đầu tiên của mỗi sản phẩm
        for (Product product : products.getContent()) {
            ProductImage firstImage = productImageService.findFirstByProductId(product.getId());
            if (firstImage != null) {
                productFirstImageMap.put(product.getId(), firstImage);
            }
        }

        // Lấy tất cả ảnh của từng sản phẩm
        for (Product product : productsList) {
            List<ProductImage> images = productImageService.findByProductId(product.getId());
            if (images != null) {
                productImagesMap.put(product.getId(), images);
            }
        }

        model.addAttribute("productsPage", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("productFirstImageMap", productFirstImageMap);
        model.addAttribute("productImagesMap", productImagesMap);

        return "/web/index";
    }

    @GetMapping("/product/{id}/{slug}")
    public String ProductDetail(@PathVariable Integer id, @PathVariable String slug, Model model) {
        Product product = productService.getProductDetail(id, slug);
        List<ProductImage> productImages = productService.getImageByProductId(id);
        List<Product> get3product = productService.getTop3Product(id, slug);
        List<Product> productsListImg = productService.findAllByStatusOrderByCreatedAtDesc(true);
        // Lấy ảnh cho 3 sản phẩm tương tự
        Map<Integer, List<ProductImage>> similarProductImages = new HashMap<>();

        //luu tat ca anh
        Map<Integer, List<ProductImage>> productImagesMapOutFit = new HashMap<>();

        for (Product similarProduct : get3product) {
            List<ProductImage> images = productService.getImageByProductId(similarProduct.getId());
            similarProductImages.put(similarProduct.getId(), images);
        }


        // Lấy tất cả ảnh của từng sản phẩm
        for (Product products : productsListImg) {
            List<ProductImage> images = productImageService.findByProductId(products.getId());
            if (images != null) {
                productImagesMapOutFit.put(products.getId(), images);
            }
        }
        //
        List<Product> productCategory = productService.findTop4ByCategoryAndStatus(product.getCategory().getId(), id);

        List<Color> colors = productVariantService.getProductColors(id);
        List<Size> sizes = productVariantService.getProductSizes(id);
        List<ProductVariant> variants = productVariantService.getProductVariants(id);

        model.addAttribute("productsDetail", product);
        model.addAttribute("productImages", productImages);
        model.addAttribute("get3product", get3product);
        model.addAttribute("similarProductImages", similarProductImages);
        model.addAttribute("productImagesMapOutFit", productImagesMapOutFit);
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
    public String CheckoutPage(Model model, @RequestParam(required = false) Integer provinceId) {
        // Lấy danh sách tỉnh
        List<Province> provinces = provinceService.getAllProvince();

        // Lấy danh sách quận/huyện dựa trên provinceId (nếu có)
        List<District> districts = (provinceId != null) ? districtService.getDistrictByProvinceId(provinceId) : new ArrayList<>();

        // Lấy danh sách xã/phường (nếu có districtId)
        List<Ward> wards = (provinceId != null) ? wardService.getWardByDistrictId(provinceId) : new ArrayList<>();

        // Thêm các đối tượng vào model
        model.addAttribute("provinces", provinces);
        model.addAttribute("districts", districts);
        model.addAttribute("wards", wards);

        return "/web/checkout";
    }

    // Endpoint lấy danh sách tất cả các tỉnh
    @GetMapping("/provinces")
    public List<Province> getAllProvinces() {
        return provinceService.getAllProvince();
    }

    // Endpoint lấy các quận theo provinceId
    @GetMapping("/districts/{provinceId}")
    public ResponseEntity<List<District>> getDistrictsByProvince(@PathVariable Integer provinceId) {
        try {
            List<District> districts = districtService.getDistrictsByProvince(provinceId);
            if (districts != null && !districts.isEmpty()) {
                return ResponseEntity.ok(districts);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Endpoint lấy các phường theo districtId
    @GetMapping("/wards/{districtId}")
    public ResponseEntity<List<Ward>> getWardsByDistrictId(@PathVariable Integer districtId) {
        try {
            List<Ward> wards = wardService.getWardByDistrict(districtId);
            if (wards != null && !wards.isEmpty()) {
                return ResponseEntity.ok(wards);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/404")
    public String Page404() {
        return "/web/404";
    }


}
