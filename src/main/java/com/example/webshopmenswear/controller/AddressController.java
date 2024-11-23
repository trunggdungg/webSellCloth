package com.example.webshopmenswear.controller;

import com.example.webshopmenswear.entity.*;
import com.example.webshopmenswear.repository.DistrictRepository;
import com.example.webshopmenswear.repository.ProvinceRepository;
import com.example.webshopmenswear.repository.UserRepository;
import com.example.webshopmenswear.repository.WardRepository;
import com.example.webshopmenswear.service.AddressService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private WardRepository wardRepository;

    @PostMapping("/save-address")
    public ResponseEntity<String> saveAddress(@RequestBody Map<String, Object> addressData, HttpSession session) {
        // Lấy user từ session
        User currentUser = (User) session.getAttribute("CURRENT_USER");
        if (currentUser == null) {
            return ResponseEntity.status(400).body("User not logged in");
        }
        Integer provinceId = (Integer) addressData.get("provinceId");
        Integer districtId = (Integer) addressData.get("districtId");
        Integer wardId = (Integer) addressData.get("wardId");
        String street = (String) addressData.get("street");
        if (provinceId == null || districtId == null || wardId == null || street == null) {
            return ResponseEntity.status(400).body("Missing required fields");
        }

        // Tìm Province, District, Ward từ database
        Province province = provinceRepository.findById(provinceId).orElse(null);
        District district = districtRepository.findById(districtId).orElse(null);
        Ward ward = wardRepository.findById(wardId).orElse(null);

        if (province == null || district == null || ward == null) {
            return ResponseEntity.status(404).body("Province, District, or Ward not found");
        }

        // Tạo đối tượng Address
        Address address = Address.builder()
                .street(street)
                .createdAt(LocalDateTime.now())
                .user(currentUser)
                .province(province)
                .district(district)
                .ward(ward)
                .build();

        // Lưu Address vào database
        addressService.saveAddress(address);
        return ResponseEntity.ok("Address saved successfully!");
    }

    // API lấy địa chỉ theo userId từ session
    @GetMapping("/user")
    @ResponseBody
    public List<Map<String, String>> getAddressesByUserId(HttpSession session) {
        User currentUser = (User) session.getAttribute("CURRENT_USER");

        // Kiểm tra xem user có tồn tại trong session hay không
        if (currentUser != null) {
            // Lấy danh sách địa chỉ của user
            List<Address> addresses = addressService.getAddressesByUserId(currentUser.getId());

            // Tạo danh sách địa chỉ với tên đầy đủ để trả về cho frontend
            List<Map<String, String>> addressList = new ArrayList<>();
            for (Address address : addresses) {
                Map<String, String> addressMap = new HashMap<>();
                addressMap.put("id", address.getId().toString());
                addressMap.put("address", address.getStreet() + ", "
                        + address.getWard().getWardName() + ", "
                        + address.getDistrict().getDistrictName() + ", "
                        + address.getProvince().getProvinceName());
                addressList.add(addressMap);
            }

            return addressList;
        } else {
            // Nếu không có user trong session, trả về danh sách trống
            return new ArrayList<>();
        }
    }
}
