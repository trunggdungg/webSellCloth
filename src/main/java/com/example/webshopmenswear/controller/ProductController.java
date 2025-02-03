package com.example.webshopmenswear.controller;

import com.example.webshopmenswear.entity.Product;
import com.example.webshopmenswear.repository.ProductRepository;
import com.example.webshopmenswear.service.CategoryService;
import com.example.webshopmenswear.service.ProductImageService;
import com.example.webshopmenswear.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/product")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ProductImageService productImageService;
    private final ProductRepository productRepository;

    @GetMapping
    public String getProductPage(Model model) {
        model.addAttribute("products", productService.getAllProduct());
        return "/admin/product/index";
    }

    @GetMapping("/create")
    public String createProductPage(Model model) {
        model.addAttribute("categories", categoryService.getAllCategory());
        return "/admin/product/create";
    }

    @GetMapping("/detail/{id}")
    public String getProductDetailPage(@PathVariable Integer id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("images", productImageService.getListImagesProduct(id));
        return "/admin/product/detail";
    }

    @GetMapping("/export")
    public void exportProductsToExcel(HttpServletResponse response) throws IOException {
        // Cấu hình Response
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=products.xlsx");

        // Lấy danh sách tất cả sản phẩm từ service
        List<Product> products = productRepository.findAll();

        // Tạo Workbook và Sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Products");

        // Tạo tiêu đề cột
        String[] header = {"Name Product", "Category", "Status", "Create At"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < header.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(header[i]);
        }

        // Dữ liệu sản phẩm
        int rowCount = 1;
        for (Product product : products) {
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(product.getName());
            row.createCell(1).setCellValue(product.getCategory().getName());
            row.createCell(2).setCellValue(product.getStatus() ? "Công khai" : "Nháp");
            row.createCell(3).setCellValue(product.getCreatedAt().toString()); // Chuyển đổi ngày nếu cần
        }

        // Xuất file Excel
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
