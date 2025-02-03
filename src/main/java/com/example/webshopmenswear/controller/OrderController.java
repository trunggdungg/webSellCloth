package com.example.webshopmenswear.controller;

import com.example.webshopmenswear.entity.Order;
import com.example.webshopmenswear.entity.OrderDetail;
import com.example.webshopmenswear.repository.OrderRepository;
import com.example.webshopmenswear.service.OrderDetailService;
import com.example.webshopmenswear.service.OrderService;
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
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final OrderRepository orderRepository;

    @GetMapping
    public String getOrderPage(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());

        return "/admin/order/index";
    }

    @GetMapping("/detail/{id}")
    public String getOrderDetailPage(@PathVariable("id") Integer orderId, Model model) {
        // Lấy thông tin Order theo ID
        Order order = orderService.getOrderById(orderId);
        // Lấy danh sách OrderDetail theo Order ID
        List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(orderId);
        // Truyền dữ liệu vào model để gửi đến view
        model.addAttribute("orderDetails", orderDetails);

        // Trả về template chi tiết đơn hàng
        return "/admin/order/detail";
    }

    @GetMapping("/create")
    public String createOrderPage() {
        return "/admin/order/create";
    }

    @GetMapping("/export")
    public void exportOrdersToExcel(HttpServletResponse response) throws IOException {
        // Cấu hình Response
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=products.xlsx");

        // Lấy dữ liệu từ Service
        List<Order> orders = orderRepository.findAll();

        // Tạo Workbook và Sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Orders");

        // Tạo dòng tiêu đề
        String[] headers = {"Name User", "Address", "Address Detail", "Total Price", "Order Status", "Create At"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Điền dữ liệu
        int rowCount = 1;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        for (Order order : orders) {
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(order.getUser().getFullName());
            row.createCell(1).setCellValue(
                order.getAddressUser().getDistrict().getDistrictName() + ", " +
                    order.getAddressUser().getProvince().getProvinceName() + ", " +
                    order.getAddressUser().getWard().getWardName()
            );
            row.createCell(2).setCellValue(order.getAddressUser().getStreet());
            row.createCell(3).setCellValue(order.getTotalPrice());
            row.createCell(4).setCellValue(order.getOrderStatus().toString());
            row.createCell(5).setCellValue(dateFormat.format(order.getCreatedAt()));
        }

        // Ghi file
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
