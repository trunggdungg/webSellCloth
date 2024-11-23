package com.example.webshopmenswear.service;

import com.example.webshopmenswear.entity.Province;
import com.example.webshopmenswear.repository.ProvinceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProvinceService {
    @Autowired
    private ProvinceRepository provinceRepository; // Đảm bảo ProvinceRepository được inject vào đây

    public List<Province> getAllProvince() {
        return provinceRepository.findAll(); // Phương thức gọi Repository
    }
}
