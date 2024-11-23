package com.example.webshopmenswear.service;

import com.example.webshopmenswear.entity.District;
import com.example.webshopmenswear.repository.DistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DistrictService {
    private final DistrictRepository districtRepository;

    public List<District> getDistrictByProvinceId(Integer provinceId) {
        return districtRepository.findByProvinceId(provinceId);
    }


    public List<District> getDistrictsByProvince(Integer provinceId) {
        return districtRepository.findByProvinceId(provinceId);
    }
}
