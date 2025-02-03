package com.example.webshopmenswear.controller;


import com.example.webshopmenswear.entity.User;
import com.example.webshopmenswear.model.Enum.UserRole;
import com.example.webshopmenswear.repository.UserRepository;
import com.example.webshopmenswear.service.AuthService;
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
@RequestMapping("/admin/user")
public class UserController {
    private final AuthService authService;
    private final UserRepository userRepository;

    @GetMapping
    public String getUserPage(Model model) {
        model.addAttribute("users", authService.getAllUsers());
        return "/admin/user/index";
    }

    @GetMapping("/create")
    public String createUserPage(Model model) {
        UserRole[] roles = UserRole.values();
        model.addAttribute("roles", roles);
        System.out.println("roles" + roles.toString());
        return "/admin/user/create";
    }

    @GetMapping("/detail/{id}")
    public String getUserDetailPage(@PathVariable Integer id, Model model) {
        UserRole[] roles = UserRole.values();
        model.addAttribute("roles", roles);
        model.addAttribute("user", authService.getUserById(id));
        return "/admin/user/detail";
    }

    @GetMapping("/export-users")
    public void exportUsersToExcel(HttpServletResponse response) throws IOException {
        // Cấu hình Response
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=users.xlsx");

        // Lấy danh sách người dùng từ service
        List<User> users = userRepository.findAll();

        // Tạo Workbook và Sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Users");

        // Tạo hàng tiêu đề
        String[] header = {"Email", "Full Name", "User Name", "Password", "Phone", "Role", "Is Active"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < header.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(header[i]);
        }

        // Ghi dữ liệu người dùng vào các hàng tiếp theo
        int rowCount = 1;
        for (User user : users) {
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(user.getEmail());
            row.createCell(1).setCellValue(user.getFullName());
            row.createCell(2).setCellValue(user.getUsername());
            row.createCell(3).setCellValue("******"); // Ẩn mật khẩu thật
            row.createCell(4).setCellValue(user.getPhoneNumber());
            row.createCell(5).setCellValue(user.getUserRole().toString());
            row.createCell(6).setCellValue(user.getIsActive() ? "Yes" : "No");
        }

        // Ghi workbook ra output stream
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
